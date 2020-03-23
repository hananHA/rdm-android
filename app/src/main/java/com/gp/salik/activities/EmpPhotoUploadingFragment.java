package com.gp.salik.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.gp.salik.BuildConfig;
import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmpPhotoUploadingFragment extends Fragment {


    View v;
    private ImageView up_photo0, up_photo1, up_photo2, up_photo3;
    Button close_ticket;
    private int ticket_id;
    private RadioButton radio_high, radio_medium, radio_normal;
    private boolean canSend = false;
    private File photoFile;
    private Uri photoURI;
    private Bitmap bitmap;
    private int photoNumber = -1;
    private int degreeOfDamageId = 0;
    ArrayList<String> filePaths = new ArrayList<>();
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;


    public EmpPhotoUploadingFragment() {
    }

    public EmpPhotoUploadingFragment(int ticket_id) {
        this.ticket_id = ticket_id;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.photo_uploading_emp_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        up_photo0 = v.findViewById(R.id.up_photo0);
        up_photo1 = v.findViewById(R.id.up_photo1);
        up_photo2 = v.findViewById(R.id.up_photo2);
        up_photo3 = v.findViewById(R.id.up_photo3);

        radio_high = v.findViewById(R.id.radio_high);
        radio_medium = v.findViewById(R.id.radio_medium);
        radio_normal = v.findViewById(R.id.radio_normal);

        radio_medium.setChecked(true);


        up_photo0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 0;
                dispatchTakePictureIntent();

            }
        });

        up_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 1;
                dispatchTakePictureIntent();
            }
        });

        up_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 2;
                dispatchTakePictureIntent();
            }
        });

        up_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 3;
                dispatchTakePictureIntent();
            }
        });

        close_ticket = v.findViewById(R.id.closeTicket);
        close_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: upload pictures

                if (radio_high.isChecked())
                    degreeOfDamageId = 1;
                else if (radio_medium.isChecked())
                    degreeOfDamageId = 2;
                else if (radio_normal.isChecked())
                    degreeOfDamageId = 3;


                if (!canSend) {
                    Log.e("ticket", "id ticket: " + ticket_id);

                    Intent intent = new Intent(getActivity(), ConfirmRed.class);
                    intent.putExtra("msg", "الرجاء رفع صورة واحدة على الأقل !");
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "الرجاء رفع صورة واحدة على الأقل !", Toast.LENGTH_LONG).show();
                    return;
                }
                if (canSend) {
                    App.confirmMessage = "تأكيد رفع الصور وإغلاق التذكرة";

                    Intent intent = new Intent(getActivity(), ConfirmGreen.class);
                    startActivity(intent);
                }

                // send request when confirm?

                sendRequest();

            }
        });

        return v;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(this, "com.camera.app.fileprovider", photoFile);
                photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        filePaths.add(mCurrentPhotoPath);
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == -1) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
                switch (photoNumber) {
                    case 0:
                        up_photo0.setImageBitmap(bitmap);
                        break;
                    case 1:
                        up_photo1.setImageBitmap(bitmap);
                        break;
                    case 2:
                        up_photo2.setImageBitmap(bitmap);
                        break;
                    case 3:
                        up_photo3.setImageBitmap(bitmap);
                        break;
                    default:
                        Toast.makeText(getActivity(), "الرجاء المحاولة مرة أخرى", Toast.LENGTH_LONG).show();
                }
                canSend = true;
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "المنصة تحت الصيانة ، الرجاء المحاولة لاحقا ", Toast.LENGTH_LONG).show();
        }
    }

    public void sendRequest() {
        //Creating a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TicketClient.BASE_URL)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < filePaths.size(); i++) {
            File photo = new File(filePaths.get(i));
            builder.addFormDataPart("photos[" + i + "]", "photos[" + i + "].jpg", RequestBody.create(MediaType.parse("multipart/form-data"), photo));
        }
        builder.addFormDataPart("degree_id", String.valueOf(degreeOfDamageId));
        builder.addFormDataPart("ticket_id", String.valueOf(ticket_id));
        builder.addFormDataPart("status", "SOLVED");


        MultipartBody requestBody = builder.build();

        //creating the api interface
        TicketClient api = retrofit.create(TicketClient.class);

        Call<ResponseBody> call = api.updateTicket(requestBody, "Bearer " + App.token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //JSONObject res = new JSONObject(response.body().to());
                    //String message = response.body().toString();
                    listTicket();

                } else {
                    if (response.code() == 422) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errMsg = jObjError.getString("message");
                            String tryError = jObjError.getString("errors");
                            if (errMsg.equalsIgnoreCase("The given data was invalid.")) {
                                Toast.makeText(getActivity(), jObjError.toString(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), tryError, Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), "الرجاء التأكد من إدخال البيانات المطلوبة بشكل صحيح", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    if (response.code() == 401) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errMsg = jObjError.getString("message");
                            if (errMsg.equalsIgnoreCase("Unauthorized")) {
                                Toast.makeText(getActivity(), "البريد الإلكتروني أو كلمة المرور غير صحيحة", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    if (response.code() == 500) {
                        Toast.makeText(getActivity(), "المنصة تحت الصيانة ، الرجاء المحاولة لاحقا ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "الرجاء التحقق من اتصالك بالإنترنت والمحاولة لاحقا ", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void listTicket() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TicketClient.BASE_URL)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        TicketClient api = retrofit.create(TicketClient.class);
        Call<JsonArray> call = api.listTicket("Bearer " + App.token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    if (response.isSuccessful()) {
                        App.listTicketResponse = response.body().toString();
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        App.TICKET_NUM = jsonArray.length();
                        Intent intent = new Intent(getActivity(), EmpMainNavActivity.class);
                        startActivity(intent);
//                      Log.d("resList", res);
                    } else {
                        if (response.code() == 422 || response.code() == 401 || response.code() == 500) {
                            Log.e("error list ticket ", "error code is: " + response.code());

                        }
                    }
                } catch (Exception e) {
                    Log.e("error when list ticket", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

}

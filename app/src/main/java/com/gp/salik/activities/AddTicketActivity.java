package com.gp.salik.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.gp.salik.BuildConfig;
import com.gp.salik.Model.App;
import com.gp.salik.Model.Neighborhood;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

public class AddTicketActivity extends Activity {

    private Button sendTicket;
    private Uri photoURI;
    private Bitmap bitmap;
    private File photoFile;
    private int photoNumber = -1;
    private boolean canSend = false;
    private Spinner spinner;
    ArrayList<String> filePaths = new ArrayList<>();

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView photo0, photo1, photo2, photo3;
    private EditText description;
    double latitude = 0.0;
    double longitude = 0.0;
    int city = 6; // city_id for Makkah is 6 in db;
    String resNeighborhoods;
    String[] spinnerArray;
    private static List<Neighborhood> neighborhoodList = new ArrayList<>();
    HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (height > 1900) {
            getWindow().setLayout((int) (width * 1.), (int) (height * .58));
        } else {
            getWindow().setLayout((int) (width * 1.), (int) (height * .7));
        }

        WindowManager.LayoutParams wlp = getWindow().getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(wlp);

        spinner = findViewById(R.id.spinner);
        photo0 = findViewById(R.id.photo0);
        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        photo3 = findViewById(R.id.photo3);
        sendTicket = findViewById(R.id.sendTicket);
        description = findViewById(R.id.description);

        getNeighborhoods();

        sendTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canSend) {
                    Intent intent = new Intent(AddTicketActivity.this, ConfirmRed.class);
                    intent.putExtra("msg", "الرجاء رفع صورة واحدة على الأقل !");
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "الرجاء رفع صورة واحدة على الأقل !", Toast.LENGTH_LONG).show();
                    return;
                }

                if (MainActivity.lat != null && MainActivity.lng != null) {
                    latitude = Double.parseDouble(MainActivity.lat);
                    longitude = Double.parseDouble(MainActivity.lng);
                }
                if (canSend) {
                    App.confirmMessage = "تأكيد إضافة التذكرة";
                    Intent intent = new Intent(AddTicketActivity.this, ConfirmGreen.class);
                    startActivity(intent);
                }
                // send request when confirm?
                sendRequest();
            }
        });

        photo0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 0;
                dispatchTakePictureIntent();

            }
        });

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 1;
                dispatchTakePictureIntent();

            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 2;
                dispatchTakePictureIntent();

            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoNumber = 3;
                dispatchTakePictureIntent();

            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(this, "com.camera.app.fileprovider", photoFile);
                photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
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
        builder.addFormDataPart("latitude", String.valueOf(latitude));
        builder.addFormDataPart("longitude", String.valueOf(longitude));
        builder.addFormDataPart("city", String.valueOf(city));
        builder.addFormDataPart("neighborhood", String.valueOf(spinnerMap.get(spinner.getSelectedItemPosition())));
        if (!description.getText().toString().trim().isEmpty()) {
            builder.addFormDataPart("description", description.getText().toString());
        }
        MultipartBody requestBody = builder.build();

        //creating the api interface
        TicketClient api = retrofit.create(TicketClient.class);

        Call<ResponseBody> call = api.addTicket(requestBody, "Bearer " + App.token);

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
                                Toast.makeText(getApplicationContext(), jObjError.toString(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), tryError, Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), "الرجاء التأكد من إدخال البيانات المطلوبة بشكل صحيح", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    if (response.code() == 401) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errMsg = jObjError.getString("message");
                            if (errMsg.equalsIgnoreCase("Unauthorized")) {
                                Toast.makeText(getApplicationContext(), "البريد الإلكتروني أو كلمة المرور غير صحيحة", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    if (response.code() == 500) {
                        Toast.makeText(getApplicationContext(), "المنصة تحت الصيانة ، الرجاء المحاولة لاحقا ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "الرجاء التحقق من اتصالك بالإنترنت والمحاولة لاحقا ", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                switch (photoNumber) {
                    case 0:
                        photo0.setImageBitmap(bitmap);
                        break;
                    case 1:
                        photo1.setImageBitmap(bitmap);
                        break;
                    case 2:
                        photo2.setImageBitmap(bitmap);
                        break;
                    case 3:
                        photo3.setImageBitmap(bitmap);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "الرجاء المحاولة مرة أخرى", Toast.LENGTH_LONG).show();
                }
                canSend = true;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "المنصة تحت الصيانة ، الرجاء المحاولة لاحقا ", Toast.LENGTH_LONG).show();
        }
    }

    public void setNeighborhoods() {
        resNeighborhoods = App.sharedPreferences.getString("neighborhoodsResponse", null);
        Type listType = new TypeToken<List<Neighborhood>>() {

        }.getType();
        neighborhoodList = getNeFromJson(resNeighborhoods, listType);
        spinnerArray = new String[neighborhoodList.size()];
        for (int i = 0; i < neighborhoodList.size(); i++) {
            spinnerMap.put(i, neighborhoodList.get(i).getId());
            spinnerArray[i] = neighborhoodList.get(i).getName_ar();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void getNeighborhoods() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TicketClient.BASE_URL)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        TicketClient api = retrofit.create(TicketClient.class);

        Call<JsonArray> call = api.getNeighborhoods("Bearer " + App.token);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    if (response.isSuccessful()) {
                        SharedPreferences.Editor editUserInfo = App.sharedPreferences.edit();
                        Log.d("poblem", response.body().toString());
                        editUserInfo.putString("neighborhoodsResponse", response.body().toString());
                        editUserInfo.apply();
                        setNeighborhoods();
                    }
                    if (response.code() == 422 || response.code() == 401 || response.code() == 500) {
                        Log.e("error ", "error code is: " + response.code());
                    }
                } catch (Exception e) {
                    Log.e("error when ", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "الرجاء التحقق من اتصالك بالإنترنت والمحاولة لاحقا ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static <T> List<T> getNeFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
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
                        Intent intent = new Intent(AddTicketActivity.this, MainNavActivity.class);
                        startActivity(intent);
                        finish();
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

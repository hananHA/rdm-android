package com.example.rdm.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rdm.BuildConfig;
import com.example.rdm.Model.App;
import com.example.rdm.R;
import com.example.rdm.api.TicketClient;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTicketActivity extends AppCompatActivity {

    private ImageView photo1, photoTest;
    private EditText description;
    private Button sendTicket;
    private Uri photoURI;
    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private File photoFile = null;

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.camera.app.fileprovider",
//                        photoFile);
                photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


            }
        }
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {


//            Uri uri = data.getData();
//            filePath = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
//               bitmap = (Bitmap) data.getExtras().get("data");

                photo1.setImageBitmap(bitmap);
                photoTest.setImageBitmap(bitmap);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

            }


//            photo1.setImageBitmap((Bitmap) data.getExtras().get("data"));


            Toast.makeText(getApplicationContext(), mCurrentPhotoPath, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "المنصة تحت الصيانة ، الرجاء المحاولة لاحقا ", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        photo1 = findViewById(R.id.photo1);
        description = findViewById(R.id.description);
        sendTicket = findViewById(R.id.sendTicket);
        //photoTest = findViewById(R.id.photoTest);


        sendTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = description.getText().toString();
                double latitude = 0.0;
                double longitude = 0.0;

                if (MainActivity.lat != null && MainActivity.lng != null) {
                    latitude = Double.parseDouble(MainActivity.lat);
                    longitude = Double.parseDouble(MainActivity.lng);
                }


                int city = 6; // city_id for makkah is 6 in db;
                int neighborhood = 3424; // neighborhood_id for alzaher - makkah (just test);

                final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                httpClient.addInterceptor(logging);

                //Creating a retrofit object
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TicketClient.BASE_URL)
                        //Here we are using the GsonConverterFactory to directly convert json data to object
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();


                //creating the api interface
                TicketClient api = retrofit.create(TicketClient.class);

                File file = new File(mCurrentPhotoPath);

                RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("photos[0]", "test.jpg", RequestBody.create(MediaType.parse("multipart/form-data"), photoFile));


                Call<ResponseBody> call = api.addTicket(latitude, longitude, city, neighborhood, filePart, "Bearer " + App.token);
//                Call<ResponseBody> call = api.addTicket(file, "djowsj");

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            JSONObject res = new JSONObject(response.body().to());

//                            String message = response.body().toString();
                            Toast.makeText(getApplicationContext(), "تمت إضافة التذكرة بنجاح !", Toast.LENGTH_LONG).show();


                        } else {

                            if (response.code() == 422) {

                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    String errMsg = jObjError.getString("message");

                                    String tryError = jObjError.getString("errors");
                                    if (errMsg.equalsIgnoreCase("The given data was invalid.")) {
                                        Toast.makeText(getApplicationContext(), jObjError.toString(), Toast.LENGTH_LONG).show();

//                                        Toast.makeText(getApplicationContext(), tryError, Toast.LENGTH_LONG).show();

//                                        Toast.makeText(getApplicationContext(), "الرجاء التأكد من إدخال البيانات المطلوبة بشكل صحيح", Toast.LENGTH_LONG).show();

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


                    }
                });

            }
        });


        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 100);


            }
        });


        Log.d("Hello world", "Hello");
        Toast.makeText(getApplicationContext(), " Lat :  " + MainActivity.lat + " and Lng : " + MainActivity.lng, Toast.LENGTH_LONG).show();

    }
}

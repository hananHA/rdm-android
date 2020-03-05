package com.gp.salik.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gp.salik.Model.App;
import com.gp.salik.Model.Neighborhood;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.gp.salik.R;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.gp.salik.BuildConfig;
import com.gp.salik.api.TicketClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private boolean opened;
    private ConstraintLayout addTicketCard;
    private ConstraintLayout confirmCard;
    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    public static String lat;
    public static String lng;
    private Button logoutButton, addTicketButton, sendTicket;
    private Uri photoURI;
    private Bitmap bitmap;
    private File photoFile;
    private int photoNumber = -1;
    private boolean canSend = false;
    private Spinner spinner;
    ArrayList<String> filePaths = new ArrayList<>();

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView photo0, photo1, photo2, photo3, profile_image;
    private EditText description;
    double latitude = 0.0;
    double longitude = 0.0;
    int city = 6; // city_id for makkah is 6 in db;
    String resNeighborhoods;
    String[] spinnerArray;
    public static List<Neighborhood> neighborhoodList = new ArrayList<>();
    HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();
    private SupportMapFragment mapFragment;
    private RelativeLayout hintLayout;
    private View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        addTicketCard = findViewById(R.id.view);
        addTicketCard.setVisibility(View.INVISIBLE);
        confirmCard = findViewById(R.id.confirm);
        confirmCard.setVisibility(View.INVISIBLE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        //mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermissions();
        getLastLocation();

        //logoutButton = findViewById(R.id.logoutButton);
        addTicketButton = findViewById(R.id.addTicket);
        spinner = findViewById(R.id.spinner);

        photo0 = findViewById(R.id.photo0);
        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        photo3 = findViewById(R.id.photo3);
        sendTicket = findViewById(R.id.sendTicket);
        description = findViewById(R.id.description);
        profile_image = findViewById(R.id.profile_image);

        getNeighborhoods();
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listTicket();
//                Intent intent = new Intent(MainActivity.this, MainNavActivity.class);
//                startActivity(intent);

            }
        });


        addTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!opened) {
                    addTicketCard.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            addTicketCard.getHeight(),
                            0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    addTicketCard.startAnimation(animate);
                    addTicketButton.setVisibility(View.INVISIBLE);
                    opened = !opened;
                }

            }
        });

        sendTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canSend) {
                    confirmCard.setBackgroundResource(R.drawable.confirm_red);
                    confirmCard.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            addTicketCard.getHeight(),
                            0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    confirmCard.startAnimation(animate);
                    //Toast.makeText(getApplicationContext(), "الرجاء رفع صورة واحدة على الأقل !", Toast.LENGTH_LONG).show();
                    return;
                }


                if (MainActivity.lat != null && MainActivity.lng != null) {
                    latitude = Double.parseDouble(MainActivity.lat);
                    longitude = Double.parseDouble(MainActivity.lng);
                }

                if(canSend) {
                    confirmCard.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            addTicketCard.getHeight(),
                            0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    confirmCard.startAnimation(animate);
                }


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

        hintLayout = findViewById(R.id.hint);
        hintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintLayout.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful()) {
                                    Location location = task.getResult();
                                    if (location == null) {
                                        requestNewLocationData();
                                    } else {
                                        lat = location.getLatitude() + "";
                                        lng = location.getLongitude() + "";
                                        mapFragment.getMapAsync(MainActivity.this);
                                    }

                                } else {
                                    System.out.println("000000000000000");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = mLastLocation.getLatitude() + "";
            lng = mLastLocation.getLongitude() + "";
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("im here");
        googleMap.setMyLocationEnabled(true);
        googleMap.setMinZoomPreference(14);
        LatLng current = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 250);
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(addTicketCard.getVisibility() == View.VISIBLE) {
                    addTicketCard.setVisibility(View.INVISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            0,
                            addTicketCard.getHeight());
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    addTicketCard.startAnimation(animate);
                    addTicketButton.setVisibility(View.VISIBLE);
                    opened = !opened;
                }

            }
        });
        System.out.println(lat);
        System.out.println(lng);
    }

    @Override

    public void onResume() {
        super.onResume();
        getLastLocation();

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


//                creating the api interface
        TicketClient api = retrofit.create(TicketClient.class);

        Call<ResponseBody> call = api.addTicket(requestBody, "Bearer " + App.token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
//                            JSONObject res = new JSONObject(response.body().to());

//                            String message = response.body().toString();
                    Toast.makeText(getApplicationContext(), "تمت إضافة التذكرة بنجاح !", Toast.LENGTH_LONG).show();
                    listTicket();


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


                        Intent intent = new Intent(MainActivity.this, MainNavActivity.class);
                        startActivity(intent);


//                        Log.d("resList", res);

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

//
}

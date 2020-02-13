package com.example.rdm.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rdm.R;

import java.io.File;
import java.io.IOException;

import retrofit2.http.Url;

public class AddTicketActivity extends AppCompatActivity {

    private ImageView photo1;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            photo1.setImageBitmap((Bitmap) data.getExtras().get("data"));
            Toast.makeText(getApplicationContext(), "OKKKK", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "المنصة تحت الصيانة ، الرجاء المحاولة لاحقا ", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        photo1 = findViewById(R.id.photo1);

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);


            }
        });


        Log.d("Hello world", "Hello");
        Toast.makeText(getApplicationContext(), " Lat :  " + MainActivity.lat + " and Lng : " + MainActivity.lng, Toast.LENGTH_LONG).show();

    }
}

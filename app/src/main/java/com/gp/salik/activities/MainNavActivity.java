package com.gp.salik.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.gp.salik.Model.App;
import com.gp.salik.Model.ViewPagerAdapter;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainNavActivity extends AppCompatActivity {

    static ViewPager viewPager;
    TextView logoutButton, backButton, helloCircle, settingsCircle, ticketsCircle;
    static ViewPagerAdapter vpadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        vpadapter = new ViewPagerAdapter(getSupportFragmentManager());

        logoutButton = findViewById(R.id.logout_btn);
        backButton = findViewById(R.id.back_to_map);
        helloCircle = findViewById(R.id.hello_circle);
        settingsCircle = findViewById(R.id.settings_circle);
        ticketsCircle = findViewById(R.id.tickets_circle);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(vpadapter);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editUserInfo = App.sharedPreferences.edit();
                editUserInfo.putString("token", null);
                editUserInfo.apply();

                // TODO: make sure the activities are all closed
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ticketsCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpadapter = new ViewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(vpadapter);
                viewPager.setCurrentItem(0);
            }
        });

        settingsCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    public static ViewPager getViewPager() {
        return viewPager;
    }

    public static ViewPagerAdapter getViewPagerAdapter() {
        return vpadapter;
    }

}

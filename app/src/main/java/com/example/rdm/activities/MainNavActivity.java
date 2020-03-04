package com.example.rdm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.rdm.Model.App;
import com.example.rdm.Model.ViewPagerAdapter;
import com.example.rdm.R;

public class MainNavActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView logoutButton, backButton, helloCircle, settingsCircle, ticketsCircle;
    ViewPagerAdapter vpadapter;

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

        //vpadapter.addFragment(new TicketsList());
        //vpadapter.addFragment(new AccountSettings());

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

                Intent mainIntent = new Intent(MainNavActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ticketsCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}

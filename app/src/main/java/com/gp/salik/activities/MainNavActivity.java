package com.gp.salik.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.gp.salik.Model.App;
import com.gp.salik.Model.ViewPagerAdapter;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;
import com.gp.salik.api.UserClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainNavActivity extends AppCompatActivity {

    static ViewPager viewPager;
    TextView logoutButton, backButton, helloCircle, settingsCircle, ticketsCircle, user_name, tickets_num;
    static ViewPagerAdapter vpadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        vpadapter = new ViewPagerAdapter(getSupportFragmentManager());

        logoutButton = findViewById(R.id.logout_btn);
        backButton = findViewById(R.id.back_to_map);
        helloCircle = findViewById(R.id.hello_circle);
        settingsCircle = findViewById(R.id.settings_circle);
        ticketsCircle = findViewById(R.id.tickets_circle);
        user_name = findViewById(R.id.user_name);
        tickets_num = findViewById(R.id.tickets_num);
        tickets_num.setText(String.valueOf(App.TICKET_NUM));

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(vpadapter);

        if (App.USER_NAME == null || App.USER_NAME.equalsIgnoreCase("null")) {
            user_name.setText("");

        } else {
            String mystring = App.USER_NAME;
            String arr[] = mystring.split(" ", 2);

            String firstWord = arr[0];   //first name of user

            user_name.setText(firstWord);
        }


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
                editUserInfo.remove("token");
                editUserInfo.remove("name");
                editUserInfo.remove("email");
                editUserInfo.remove("phone");
                editUserInfo.remove("neighborhood_id");
                editUserInfo.remove("gender");
                editUserInfo.remove("neighborhoodsResponse");
                editUserInfo.remove("hi");
                Log.e("all good", "good");
                editUserInfo.apply();

                // TODO: make sure the activities are all closed
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                logOut();
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        ticketsCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getSupportFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new TicketsListFragment());
                trans.commit();
            }
        });

        settingsCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getSupportFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new AccountSettingsFragment());
                trans.commit();
            }
        });
    }


    private void logOut() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserClient.BASE_URL)
                //Here we are using the GsonConverterFactory to directly convert json data to object
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.okHttpClientCall().build())
                .build();

        UserClient api = retrofit.create(UserClient.class);
        Call<ResponseBody> call = api.logout("Bearer " + App.token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {

                    } else {
                        if (response.code() == 422 || response.code() == 401 || response.code() == 500 || response.code() == 400) {
                            Toast.makeText(getApplicationContext(), "الرجاء التحقق من حالة الحساب ", Toast.LENGTH_LONG).show();


                        }
                    }
                } catch (Exception e) {
                    Log.e("error when rate ticket", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "الرجاء التحقق من الاتصال بالإنترنت ", Toast.LENGTH_LONG).show();

            }
        });

    }

}

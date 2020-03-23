package com.gp.salik.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.gp.salik.Model.App;
import com.gp.salik.Model.ViewPagerAdapter;
import com.gp.salik.R;
import com.gp.salik.api.UserClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gp.salik.Model.App.USER_ROLE;

public class EmpMainNavActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView logoutCircle, ticketsCircle, emp_name, emp_tickets_num;
    private ViewPagerAdapter vpadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_emp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        Log.e("roooooolllllleeeee",USER_ROLE);

        viewPager = findViewById(R.id.viewpager_emp);

        vpadapter = new ViewPagerAdapter(getSupportFragmentManager());

        logoutCircle = findViewById(R.id.logout_circle);
        ticketsCircle = findViewById(R.id.emp_tickets_circle);
        emp_name = findViewById(R.id.emp_name);
        emp_tickets_num = findViewById(R.id.emp_tickets_num);
        emp_tickets_num.setText(String.valueOf(App.TICKET_NUM));

        viewPager.setAdapter(vpadapter);

        if (App.USER_NAME == null || App.USER_NAME.equalsIgnoreCase("null")) {
            emp_name.setText("");

        } else {
            String mystring = App.USER_NAME;
            String arr[] = mystring.split(" ", 2);

            String firstWord = arr[0];   //first name of user

            emp_name.setText(firstWord);
        }

        Toast.makeText(getApplicationContext() , "your emp" , Toast.LENGTH_LONG).show();


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        logoutCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: logout
                SharedPreferences.Editor editUserInfo = App.sharedPreferences.edit();
                editUserInfo.remove("token");
                editUserInfo.remove("name");
                editUserInfo.remove("email");
                editUserInfo.remove("phone");
                editUserInfo.remove("neighborhood_id");
                editUserInfo.remove("gender");
                editUserInfo.remove("neighborhoodsResponse");
                editUserInfo.remove("role_id");
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

        ticketsCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getSupportFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new EmpTicketListFragment());
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

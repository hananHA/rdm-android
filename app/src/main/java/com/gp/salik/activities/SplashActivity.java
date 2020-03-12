package com.gp.salik.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.gp.salik.api.TicketClient;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Log.d("User Token: ", "user_token: " + App.token + "Null !");
        App.sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        App.token = App.sharedPreferences.getString("token", null);
        App.USER_NAME = App.sharedPreferences.getString("name", null);
        App.USER_EMAIL = App.sharedPreferences.getString("email", null);
        App.USER_PHONE = App.sharedPreferences.getString("phone", null);

        if (App.token != null) {
            listTicket();

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this, RegisterActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, 1500);
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
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();


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
                Toast.makeText(getApplicationContext(), "الرجاء التحقق من اتصالك بالإنترنت والمحاولة لاحقا ", Toast.LENGTH_LONG).show();

            }
        });


    }
}

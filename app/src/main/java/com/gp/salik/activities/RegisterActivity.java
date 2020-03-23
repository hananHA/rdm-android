package com.gp.salik.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.gp.salik.Model.User;
import com.gp.salik.api.TicketClient;
import com.gp.salik.api.UserClient;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText email, password, passwordCon;
    private TextView loginText;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        registerButton = findViewById(R.id.registerButton);
        email = findViewById(R.id.emailReg);
        password = findViewById(R.id.passwordReg);
        passwordCon = findViewById(R.id.passwordCon);
        loginText = findViewById(R.id.registerText);

//        password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

//        password.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString().trim();
                String p = password.getText().toString();
                String pc = passwordCon.getText().toString();

                if (e.isEmpty() && p.isEmpty() && pc.isEmpty()) {
                    email.setError("الرجاء كتابة البريد الإلكتروني");
                    email.requestFocus();

                    password.setError("الرجاء كتابة كلمة المرور");
                    password.requestFocus();

                    passwordCon.setError("الرجاء تأكيد كلمة المرور");
                    passwordCon.requestFocus();
                } else if (e.isEmpty()) {
                    email.setError("الرجاء كتابة البريد الإلكتروني");
                    email.requestFocus();
                } else if (!e.matches(emailPattern)) {
                    email.setError("الرجاء كتابة بريد إلكتروني صحيح");
                    email.requestFocus();
                } else if (p.isEmpty()) {
                    password.setError("الرجاء كتابة كلمة المرور");
                    password.requestFocus();
                } else if (pc.isEmpty()) {
                    passwordCon.setError("الرجاء تأكيد كلمة المرور");
                    passwordCon.requestFocus();
                } else if (!p.equals(pc)) {
                    Toast.makeText(getApplicationContext(), "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
                } else {


                    //Creating a retrofit object
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(UserClient.BASE_URL)
                            //Here we are using the GsonConverterFactory to directly convert json data to object
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(App.okHttpClientCall().build())
                            .build();

                    //creating the api interface
                    UserClient api = retrofit.create(UserClient.class);
                    //now making the call object
                    //Here we are using the api method that we created inside the api interface
                    //Here the json data is add to a hash map with key data
                    Call<ResponseBody> call = api.postRegister(e, p, pc);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {

                                try {
                                    JSONObject user = new JSONObject(response.body().string());
                                    App.token = user.getString("access_token");
                                    if (user.getJSONObject("user_data").getString("name").isEmpty()) {
                                        App.USER_NAME = null;

                                    } else if (user.getJSONObject("user_data").getString("email").isEmpty()) {
                                        App.USER_EMAIL = null;

                                    } else if (user.getJSONObject("user_data").getString("phone").isEmpty()) {
                                        App.USER_PHONE = null;
                                    } else if (user.getJSONObject("user_data").getString("neighborhood_id").isEmpty()) {
                                        App.USER_NEIGHBORHOOD = null;
                                    } else if (user.getJSONObject("user_data").getString("gender").isEmpty()) {
                                        App.USER_GENDER = null;
                                    } else if (user.getJSONObject("user_data").getString("role_id").isEmpty()) {
                                        App.USER_ROLE = null;
                                    } else {
                                        App.USER_NAME = user.getJSONObject("user_data").getString("name");
                                        App.USER_EMAIL = user.getJSONObject("user_data").getString("email");
                                        App.USER_PHONE = user.getJSONObject("user_data").getString("phone");
                                        App.USER_NEIGHBORHOOD = user.getJSONObject("user_data").getString("neighborhood_id");
                                        App.USER_GENDER = user.getJSONObject("user_data").getString("gender");
                                        App.USER_ROLE = user.getJSONObject("user_data").getString("role_id");


                                    }

                                    SharedPreferences.Editor editUserInfo = App.sharedPreferences.edit();
                                    editUserInfo.putString("token", App.token);
                                    editUserInfo.putString("name", App.USER_NAME);
                                    editUserInfo.putString("email", App.USER_EMAIL);
                                    editUserInfo.putString("phone", App.USER_PHONE);
                                    editUserInfo.putString("neighborhood_id", App.USER_NEIGHBORHOOD);
                                    editUserInfo.putString("gender", App.USER_GENDER);
                                    editUserInfo.putString("role_id", App.USER_ROLE);


                                    editUserInfo.apply();


                                    Log.e("user info", "good " + App.USER_NAME);


                                } catch (Exception e1) {
                                    Toast.makeText(getApplicationContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("user info", e1.getMessage());

                                }


//                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), " تم تسجيلك بنجاح ", Toast.LENGTH_LONG).show();
                                getNeighborhoods();


                            } else {

                                if (response.code() == 422) {

                                    try {

                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        try {
                                            String errMsgEmail = jObjError.getJSONObject("errors").getString("email");
                                            Log.d("emad test ", errMsgEmail);
                                            if (errMsgEmail.equalsIgnoreCase("[\"The email has already been taken.\"]")) {
                                                Toast.makeText(getApplicationContext(), "البريد الإلكتروني مسجل مسبقا", Toast.LENGTH_LONG).show();

                                            } else if (errMsgEmail.equalsIgnoreCase("The given data was invalid.")) {
                                                Toast.makeText(getApplicationContext(), "الرجاء التأكد من إدخال البيانات المطلوبة بشكل صحيح", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "المنصة تحت الصيانة ، الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();

                                            }


                                        } catch (Exception e) {
                                            Log.d("emad test ", e.getMessage());

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
                            Toast.makeText(getApplicationContext(), "الرجاء التأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();


                        }
                    });

                }


            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

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
                        editUserInfo.putString("neighborhoodsResponse", response.body().toString());
                        editUserInfo.apply();
                        if (App.USER_ROLE.equalsIgnoreCase("1")) {
                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            RegisterActivity.this.startActivity(mainIntent);
                            RegisterActivity.this.finish();
                        } else {
                            listTicket();

                        }


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
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        App.TICKET_NUM = jsonArray.length();
                        Intent mainIntent = new Intent(RegisterActivity.this, EmpMainNavActivity.class);
                        RegisterActivity.this.startActivity(mainIntent);
                        RegisterActivity.this.finish();
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

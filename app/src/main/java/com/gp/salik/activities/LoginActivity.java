package com.gp.salik.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gp.salik.Model.App;
import com.gp.salik.R;
import com.gp.salik.Model.User;
import com.gp.salik.api.UserClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email, password;
    private TextView registerText, resetText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.emailLog);
        password = findViewById(R.id.passwordLog);
        registerText = findViewById(R.id.registerText);
        resetText = findViewById(R.id.resetPassword);

        resetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString().trim();
                String p = password.getText().toString();


                if (e.isEmpty() && p.isEmpty()) {
                    email.setError("الرجاء كتابة البريد الإلكتروني");
                    email.requestFocus();

                    password.setError("الرجاء كتابة كلمة المرور");
                    password.requestFocus();

                } else if (e.isEmpty()) {
                    email.setError("الرجاء كتابة البريد الإلكتروني");
                    email.requestFocus();
                } else if (!e.matches(emailPattern)) {
                    email.setError("الرجاء كتابة بريد إلكتروني صحيح");
                    email.requestFocus();
                } else if (p.isEmpty()) {
                    password.setError("الرجاء كتابة كلمة المرور");
                    password.requestFocus();
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
                    Call<User> call = api.postLogin(e, p);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                User user = response.body();
                                App.token = user.getAccess_token();

                                SharedPreferences.Editor editUserInfo = App.sharedPreferences.edit();
                                editUserInfo.putString("token", App.token);
                                editUserInfo.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), " أهلا بعودتك مرة أخرى ", Toast.LENGTH_LONG).show();
                                return;


                            } else {

                                if (response.code() == 422) {

                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        String errMsg = jObjError.getString("message");
                                        if (errMsg.equalsIgnoreCase("The given data was invalid")) {
                                            Toast.makeText(getApplicationContext(), "الرجاء التأكد من إدخال البيانات المطلوبة بشكل صحيح", Toast.LENGTH_LONG).show();

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
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "الرجاء التأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();


                        }
                    });
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

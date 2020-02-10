package com.example.rdm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rdm.R;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email, password;
    private TextView registerText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.emailLog);
        password = findViewById(R.id.passwordLog);
        registerText = findViewById(R.id.registerText);

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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }


            }

            /*
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = "http://testtamayoz.tamayyozz.net/api/login"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.POST, url,
                    Response.Listener<String> { response ->
                            // Display the first 500 characters of the response string.
                            print("Ok")
                            print(response.substring(0, 10))
                    },
                    Response.ErrorListener { error ->

                    println("Error No")
                println(error.toString())
            })

// Add the request to the RequestQueue.
                    queue.add(stringRequest); */
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


            /*
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = "http://testtamayoz.tamayyozz.net/api/login"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.POST, url,
                    Response.Listener<String> { response ->
                            // Display the first 500 characters of the response string.
                            print("Ok")
                            print(response.substring(0, 10))
                    },
                    Response.ErrorListener { error ->

                    println("Error No")
                println(error.toString())
            })

// Add the request to the RequestQueue.
                    queue.add(stringRequest); */
//
//        registerText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

/*
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });*/
    }
}

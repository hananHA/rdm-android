package com.example.rdm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rdm.R;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText email, password, passwordCon;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton);
        email = findViewById(R.id.emailReg);
        password = findViewById(R.id.passwordReg);
        passwordCon = findViewById(R.id.passwordCon);
        loginText = findViewById(R.id.registerText);

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
                } else if (p.isEmpty()) {
                    password.setError("الرجاء كتابة كلمة المرور");
                    password.requestFocus();
                } else if (pc.isEmpty()) {
                    passwordCon.setError("الرجاء تأكيد كلمة المرور");
                    passwordCon.requestFocus();
                } else if (!p.equals(pc)) {
                    Toast.makeText(getApplicationContext(), "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

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
}

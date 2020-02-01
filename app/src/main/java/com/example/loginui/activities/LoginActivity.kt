package com.example.loginui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginui.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {

            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                emailText.error = "الرجاء كتابة البريد الإلكتروني"
                emailText.requestFocus()

                passwordText.error = "الرجاء كتابة كلمة المرور"
                passwordText.requestFocus()
                return@setOnClickListener

            }


            if (email.isEmpty()) {
                emailText.error = "الرجاء كتابة البريد الإلكتروني"
                emailText.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordText.error = "الرجاء كتابة كلمة المرور"
                passwordText.requestFocus()
                return@setOnClickListener
            }


        }

        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            // تشعيل واجهة التسجيل
            startActivity(intent)


        }

    }
}

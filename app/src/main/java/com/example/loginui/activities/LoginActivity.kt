package com.example.loginui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
            queue.add(stringRequest)


        }





        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            // تشعيل واجهة التسجيل
            startActivity(intent)


        }

    }
}

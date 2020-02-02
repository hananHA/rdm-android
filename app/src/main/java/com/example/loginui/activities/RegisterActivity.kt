package com.example.loginui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginui.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)





        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // تشعيل واجهة تسجيل الدخول
            startActivity(intent)

        }

    }
}

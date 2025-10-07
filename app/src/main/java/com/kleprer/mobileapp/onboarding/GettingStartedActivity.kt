package com.kleprer.mobileapp.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.Button
import com.kleprer.mobileapp.auth.LoginActivity
import com.kleprer.mobileapp.auth.SignUpActivity1

class GettingStartedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getting_started)

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        findViewById<Button>(R.id.btn_sign_up).setOnClickListener {
            startActivity(Intent(this, SignUpActivity1::class.java))
        }
    }
}
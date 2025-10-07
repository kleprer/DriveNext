package com.kleprer.mobileapp.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.main.MainActivity
import com.kleprer.mobileapp.onboarding.GettingStartedActivity
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.AuthManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            checkWhereToGo()
        }
    }

    private suspend fun checkWhereToGo() {
        val intent = when {
            AuthManager.isFirstLaunch(this) -> {
                AuthManager.setFirstLaunchCompleted(this)
                Intent(this, GettingStartedActivity::class.java)
            }
            AuthManager.isUserLoggedIn() -> {
                Intent(this, MainActivity::class.java)
            }
            else -> {
                Intent(this, LoginActivity::class.java)
            }
        }

        startActivity(intent)
        finish()
    }
}

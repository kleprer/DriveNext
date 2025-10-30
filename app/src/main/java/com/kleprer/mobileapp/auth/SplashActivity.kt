package com.kleprer.mobileapp.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.main.MainActivity
import com.kleprer.mobileapp.onboarding.GettingStartedActivity
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.AuthManager
import com.kleprer.mobileapp.home.HomeActivity
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AuthManager.init(this)

        lifecycleScope.launch {
            delay(2500L) // 2.5 секунды задержки
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
                Intent(this, HomeActivity::class.java)
            }
            else -> {
                Intent(this, GettingStartedActivity::class.java)
            }
        }

        startActivity(intent)
        finish() // Finish splash activity
    }
}

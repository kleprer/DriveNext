package com.kleprer.mobileapp.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.databinding.ActivityLoaderBinding

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLoadingAnimation()
        simulateLoading()
    }

    private fun startLoadingAnimation() {
        val rotateAnimation = ObjectAnimator.ofFloat(binding.imageButton, "rotation", 0f, 360f)
        rotateAnimation.apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }.start()
    }

    private fun simulateLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SearchResultsActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3 секунды загрузки
    }
}
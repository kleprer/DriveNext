package com.kleprer.mobileapp.network

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.auth.SplashActivity
import com.kleprer.mobileapp.databinding.ActivityNoConnectionBinding
import com.kleprer.mobileapp.utils.NetworkUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class NoConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setupWindowInsets()
        setupClickListeners()
        observeNetworkStatus()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.viewNoConnection) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupClickListeners() {
        binding.btnTryAgain.setOnClickListener {
            checkConnectionAndProceed()
        }
    }

    private fun observeNetworkStatus() {
        lifecycleScope.launch {
            NetworkUtil.observeNetworkStatus(this@NoConnectionActivity).collectLatest { isConnected ->
                if (isConnected) {
                    proceedToMainApp()
                }
            }
        }
    }

    private fun checkConnectionAndProceed() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            proceedToMainApp()
        }
    }

    private fun proceedToMainApp() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
package com.kleprer.mobileapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.databinding.ActivityCarDetailsBinding

class CarDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
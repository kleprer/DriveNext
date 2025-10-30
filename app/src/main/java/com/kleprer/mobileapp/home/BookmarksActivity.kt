package com.kleprer.mobileapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.databinding.ActivityBookmarksBinding

class BookmarksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.databinding.ActivityHomepageBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupBottomNavigation()
    }

    private fun setupViews() {
        // Setup search functionality
        binding.searchBar.setOnClickListener {
            // Handle search bar click
        }

        // Setup book button
        binding.btnBook.setOnClickListener {
            // Handle book button click
            startActivity(Intent(this, BookingDetailsActivity::class.java))
        }

        // Setup details button
        binding.btnDetails.setOnClickListener {
            // Handle details button click
            startActivity(Intent(this, CarDetailsActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        binding.ivHome.setOnClickListener {
            // Already on home, do nothing or refresh
        }

        binding.ivBookmark.setOnClickListener {
            // Navigate to favorites/bookmarks
            startActivity(Intent(this, BookmarksActivity::class.java))
        }

        binding.ivSettings.setOnClickListener {
            // Navigate to settings
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

}
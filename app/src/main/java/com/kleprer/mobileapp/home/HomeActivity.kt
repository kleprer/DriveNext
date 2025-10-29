package com.kleprer.mobileapp.home

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import com.kleprer.mobileapp.booking.BookingActivity
import com.kleprer.mobileapp.booking.BookmarkActivity
import com.kleprer.mobileapp.booking.CarDetailsActivity
import com.kleprer.mobileapp.databinding.ActivityHomepageBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Навигация по нижнему меню
        binding.ivHome.setOnClickListener {
            // Уже на домашней странице
        }

        binding.ivBookmark.setOnClickListener {
            val intent = Intent(this, BookmarkActivity::class.java)
            startActivity(intent)
        }

        binding.ivSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Поиск
        binding.searchBar.setOnClickListener {
            val intent = Intent(this, SearchResultsActivity::class.java)
            startActivity(intent)
        }

        // Бронирование автомобиля
        binding.btnBook.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        // Детали автомобиля
        binding.btnDetails.setOnClickListener {
            val intent = Intent(this, CarDetailsActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.kleprer.mobileapp.home

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.kleprer.mobileapp.booking.BookingActivity
import com.kleprer.mobileapp.booking.BookmarkActivity
import com.kleprer.mobileapp.databinding.ActivitySettingsBinding
import com.kleprer.mobileapp.settings.AddCarActivity
import com.kleprer.mobileapp.settings.HelpActivity
import com.kleprer.mobileapp.settings.InviteFriendActivity
import com.kleprer.mobileapp.settings.NotificationsActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Навигация по нижнему меню
        binding.ivHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.ivBookmark.setOnClickListener {
            val intent = Intent(this, BookmarkActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Переход в профиль
        binding.toProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Смена темы
        binding.toThemes.setOnClickListener {
            showThemeDialog()
        }

        // Мои бронирования (заглушка)
        binding.toBookings.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        // Уведомления (заглушка)
        binding.toNotifs.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        // Добавить автомобиль (заглушка)
        binding.toAddCar.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivity(intent)
        }

        // Помощь (заглушка)
        binding.toHelp.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        // Пригласить друга (заглушка)
        binding.toAddFriend.setOnClickListener {
            val intent = Intent(this, InviteFriendActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Светлая", "Темная", "Системная")
        AlertDialog.Builder(this)
            .setTitle("Выберите тему")
            .setItems(themes) { _, which ->
                when (which) {
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            .show()
    }
}
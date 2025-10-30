package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.databinding.ActivitySettingsBinding
import com.kleprer.mobileapp.AuthManager
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()
        setupClickListeners()
        setupBottomNavigation()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val currentUser = AuthManager.getCurrentUser()
            currentUser?.let { user ->
                // Установка данных пользователя
                val fullName = "${user.firstName} ${user.lastName}".trim()
                binding.tvUsername.text = fullName.ifEmpty { "Пользователь" }
                binding.tvEmail.text = user.email

                // TODO: Загрузка аватара если есть
                // if (user.profileImagePath != null) {
                //     Glide.with(this@SettingsActivity).load(user.profileImagePath).into(binding.ivPfp)
                // }
            }
        }
    }

    private fun setupClickListeners() {
        // Переход в профиль при клике на блок профиля
        binding.toProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Бронирования
        binding.toBookings.setOnClickListener {
            // TODO: Переход на экран бронирований
            Toast.makeText(this, "Мои бронирования - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        // Тема
        binding.toThemes.setOnClickListener {
            // TODO: Переход на экран выбора темы
            Toast.makeText(this, "Выбор темы - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        // Уведомления
        binding.toNotifs.setOnClickListener {
            // TODO: Переход на экран уведомлений
            Toast.makeText(this, "Уведомления - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        // Добавление автомобиля
        binding.toAddCar.setOnClickListener {
            // TODO: Переход на экран добавления автомобиля
            Toast.makeText(this, "Подключение автомобиля - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        // Помощь
        binding.toHelp.setOnClickListener {
            // TODO: Переход на экран помощи
            Toast.makeText(this, "Помощь - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        // Пригласить друга
        binding.toAddFriend.setOnClickListener {
            // TODO: Функционал приглашения друга
            Toast.makeText(this, "Пригласить друга - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        // Стрелки next также делаем кликабельными
        binding.ivNextToProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.ivNextToBookings.setOnClickListener {
            // TODO: Переход на экран бронирований
            Toast.makeText(this, "Мои бронирования - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        binding.ivNextToTheme.setOnClickListener {
            // TODO: Переход на экран выбора темы
            Toast.makeText(this, "Выбор темы - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        binding.ivNextToNotifs.setOnClickListener {
            // TODO: Переход на экран уведомлений
            Toast.makeText(this, "Уведомления - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        binding.ivNextAddCar.setOnClickListener {
            // TODO: Переход на экран добавления автомобиля
            Toast.makeText(this, "Подключение автомобиля - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        binding.ivNextToHelp.setOnClickListener {
            // TODO: Переход на экран помощи
            Toast.makeText(this, "Помощь - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }

        binding.ivNextAddFriend.setOnClickListener {
            // TODO: Функционал приглашения друга
            Toast.makeText(this, "Пригласить друга - будет реализовано позже", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        binding.ivHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.ivBookmark.setOnClickListener {
            startActivity(Intent(this, BookmarksActivity::class.java))
        }

        binding.ivSettings.setOnClickListener {
            // Уже в настройках, обновляем данные
            loadUserData()
        }
    }

    // Добавим обработку кнопки назад устройства
    override fun onBackPressed() {
        super.onBackPressed()
        // Возврат на предыдущий экран
        finish()
    }
}
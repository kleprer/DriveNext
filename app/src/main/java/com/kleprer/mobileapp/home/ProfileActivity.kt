package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.AuthManager
import com.kleprer.mobileapp.onboarding.GettingStartedActivity
import com.kleprer.mobileapp.booking.BookmarkActivity
import com.kleprer.mobileapp.data.db.AppDatabase
import com.kleprer.mobileapp.data.repo.UserRepo
import com.kleprer.mobileapp.databinding.ActivityProfileBinding
import com.kleprer.mobileapp.databinding.DialogChangePasswordBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userRepo: UserRepo
    private var currentUser: com.kleprer.mobileapp.data.models.UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация AuthManager
        AuthManager.init(this)

        // Инициализация репозитория
        val database = AppDatabase.getInstance(this)
        userRepo = UserRepo(database.userDao(), this)

        setupClickListeners()
        loadUserData()
    }

    private fun setupClickListeners() {
        // Навигация по нижнему меню
        binding.ivHome.setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        binding.ivBookmark.setOnClickListener {
            navigateTo(BookmarkActivity::class.java)
        }

        binding.ivSettings.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

        // Смена пароля
        binding.tvChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        // Выход из профиля
        binding.tvLogOut.setOnClickListener {
            logoutUser()
        }
    }

    private fun <T> navigateTo(activityClass: Class<T>) where T : AppCompatActivity {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }

    private fun showChangePasswordDialog() {
        val dialogBinding = DialogChangePasswordBinding.inflate(layoutInflater)

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Смена пароля")
            .setView(dialogBinding.root)
            .setPositiveButton("Сменить") { _, _ ->
                val currentPassword = dialogBinding.etCurrentPassword.text.toString()
                val newPassword = dialogBinding.etNewPassword.text.toString()
                val confirmPassword = dialogBinding.etConfirmPassword.text.toString()

                if (validatePasswordChange(currentPassword, newPassword, confirmPassword)) {
                    changePassword(currentPassword, newPassword)
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    private fun validatePasswordChange(current: String, new: String, confirm: String): Boolean {
        return when {
            current.isEmpty() -> {
                showToast("Введите текущий пароль")
                false
            }
            new.isEmpty() -> {
                showToast("Введите новый пароль")
                false
            }
            new.length < 6 -> {
                showToast("Пароль должен содержать минимум 6 символов")
                false
            }
            new != confirm -> {
                showToast("Пароли не совпадают")
                false
            }
            else -> true
        }
    }

    private fun changePassword(currentPassword: String, newPassword: String) {
        lifecycleScope.launch {
            try {
                val user = currentUser
                if (user != null) {
                    if (user.password == currentPassword) {
                        // Обновляем пользователя с новым паролем
                        val updatedUser = user.copy(
                            password = newPassword,
                            updatedAt = System.currentTimeMillis()
                        )
                        userRepo.updateUser(updatedUser)
                        showToast("Пароль успешно изменен")
                    } else {
                        showToast("Неверный текущий пароль")
                    }
                } else {
                    showToast("Пользователь не найден")
                }
            } catch (e: Exception) {
                showToast("Ошибка при смене пароля: ${e.message}")
            }
        }
    }

    // ✅ РЕАЛИЗАЦИЯ ВЫХОДА С ПЕРЕХОДОМ НА АВТОРИЗАЦИЮ
    private fun logoutUser() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Выход из профиля")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Выйти") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun performLogout() {
        lifecycleScope.launch {
            try {
                val user = currentUser
                if (user != null) {
                    // Очищаем данные в БД
                    val updatedUser = user.copy(
                        accessToken = null,
                        refreshToken = null,
                        tokenExpiry = null,
                        updatedAt = System.currentTimeMillis()
                    )
                    userRepo.updateUser(updatedUser)
                }

                // Очищаем AuthManager
                AuthManager.logout()

                showToast("Вы вышли из профиля")
                navigateToAuth()

            } catch (e: Exception) {
                showToast("Ошибка при выходе: ${e.message}")
                // Все равно переходим на авторизацию даже при ошибке
                navigateToAuth()
            }
        }
    }

    private fun navigateToAuth() {
        val intent = Intent(this@ProfileActivity, GettingStartedActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                val user = userRepo.getCurrentUser()
                currentUser = user
                user?.let {
                    binding.tvUsername.text = "${it.firstName} ${it.lastName}"
                    binding.tvEmail.text = it.email
                    binding.tvGenderInfo.text = it.gender

                    val joinDate = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                        .format(Date(it.createdAt))
                    binding.tvJoinedDate.text = "Присоединился в $joinDate"
                } ?: run {
                    showToast("Данные пользователя не найдены")
                }
            } catch (e: Exception) {
                showToast("Ошибка загрузки данных: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
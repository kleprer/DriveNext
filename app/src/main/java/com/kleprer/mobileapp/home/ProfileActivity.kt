package com.kleprer.mobileapp.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.auth.LoginActivity
import com.kleprer.mobileapp.databinding.ActivityProfileBinding
import com.kleprer.mobileapp.AuthManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var currentUser: com.kleprer.mobileapp.data.models.UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()
        setupClickListeners()
        setupBottomNavigation()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            currentUser = AuthManager.getCurrentUser()
            currentUser?.let { user ->
                setupUserData(user)
            } ?: run {
                // Если пользователь не найден, возвращаем на экран входа
                Toast.makeText(this@ProfileActivity, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }
    }

    private fun setupUserData(user: com.kleprer.mobileapp.data.models.UserModel) {
        // Имя пользователя
        val fullName = "${user.firstName} ${user.lastName}".trim()
        binding.tvUsername.text = fullName.ifEmpty { "Пользователь" }

        // Email
        binding.tvEmail.text = user.email

        // Дата присоединения
        val joinDate = formatJoinDate(user.createdAt)
        binding.tvJoinedDate.text = "Присоединился $joinDate"

        // Пол
        binding.tvGenderInfo.text = when (user.gender.lowercase()) {
            "male", "мужской", "м" -> "Мужской"
            "female", "женский", "ж" -> "Женский"
            else -> user.gender
        }

        // TODO: Загрузка аватара если есть profileImagePath
        // if (user.profileImagePath != null) {
        //     Glide.with(this).load(user.profileImagePath).into(binding.ivPfp)
        // }
    }

    private fun formatJoinDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("в MMMM yyyy", Locale("ru"))
        return dateFormat.format(timestamp)
    }

    private fun setupClickListeners() {
        // Аватар - загрузка нового фото
        binding.ivPfp.setOnClickListener {
            showAvatarChangeDialog()
        }

        // Смена пароля
        binding.tvChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        // Выход из профиля
        binding.tvLogOut.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Редактирование пола
        binding.tvGenderInfo.setOnClickListener {
            showGenderSelectionDialog()
        }

        // Редактирование email
        binding.tvEmail.setOnClickListener {
            showEmailEditDialog()
        }
        binding.ibtnBack?.setOnClickListener {
            finish()
        }

    }

    private fun showAvatarChangeDialog() {
        val options = arrayOf("Сделать фото", "Выбрать из галереи", "Отмена")

        AlertDialog.Builder(this)
            .setTitle("Изменить аватар")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> chooseFromGallery()
                    // 2 - Отмена, ничего не делаем
                }
            }
            .show()
    }

    private fun takePhoto() {
        // TODO: Реализовать съемку фото с сохранением в AuthManager.updateProfileImage
        Toast.makeText(this, "Функция будет реализована позже", Toast.LENGTH_SHORT).show()
    }

    private fun chooseFromGallery() {
        // TODO: Реализовать выбор из галереи с сохранением в AuthManager.updateProfileImage
        Toast.makeText(this, "Функция будет реализована позже", Toast.LENGTH_SHORT).show()
    }

    private fun showChangePasswordDialog() {
        // TODO: Реализовать диалог смены пароля
        Toast.makeText(this, "Смена пароля будет реализована позже", Toast.LENGTH_SHORT).show()
    }

    private fun showGenderSelectionDialog() {
        val genders = arrayOf("Мужской", "Женский", "Другой")

        AlertDialog.Builder(this)
            .setTitle("Выберите пол")
            .setItems(genders) { dialog, which ->
                val selectedGender = genders[which]
                binding.tvGenderInfo.text = selectedGender
                // TODO: Сохранить выбор в базу данных
                // lifecycleScope.launch {
                //     currentUser?.let { user ->
                //         // Вызов метода для обновления пола в базе
                //     }
                // }
            }
            .show()
    }

    private fun showEmailEditDialog() {
        // TODO: Реализовать диалог редактирования email
        Toast.makeText(this, "Редактирование email будет реализовано позже", Toast.LENGTH_SHORT).show()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выход из профиля")
            .setMessage("Вы уверены, что хотите выйти из аккаунта?")
            .setPositiveButton("Выйти") { dialog, which ->
                performLogout()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun performLogout() {
        lifecycleScope.launch {
            val result = AuthManager.logoutUser(this@ProfileActivity)
            result.fold(
                onSuccess = {
                    navigateToLogin()
                    Toast.makeText(this@ProfileActivity, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    Toast.makeText(this@ProfileActivity, "Ошибка выхода: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
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

    // Обработка результата выбора фото (будет реализована позже)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                // TODO: Обработка кодов для камеры и галереи
                // lifecycleScope.launch {
                //     currentUser?.let { user ->
                //         AuthManager.updateProfileImage(user.id, imagePath)
                //     }
                // }
            }
        }
    }
}
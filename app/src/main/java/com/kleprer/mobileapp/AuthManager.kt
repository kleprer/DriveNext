package com.kleprer.mobileapp

import android.content.Context
import android.content.SharedPreferences
import com.kleprer.mobileapp.data.db.AppDatabase
import com.kleprer.mobileapp.data.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import com.kleprer.mobileapp.data.models.UserModel

object AuthManager {

    private lateinit var userRepository: UserRepo

    private const val PREFS_NAME = "auth_preferences"
    private const val KEY_CURRENT_USER_ID = "current_user_id"
    private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"

    // инициализация
    fun init(context: Context) {
        val database = AppDatabase.getInstance(context)
        userRepository = UserRepo(database.userDao())
    }

    // регистрация
    suspend fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        birthDate: String,
        gender: String,
        middleName: String? = null,
        driverLicense: String? = null,
        licenseIssueDate: String? = null
    ): Result<Long> = withContext(Dispatchers.IO) {
        val user = UserModel(
            email = email, password = password, firstName = firstName,
            lastName = lastName, middleName = middleName, birthDate = birthDate,
            gender = gender, driverLicense = driverLicense, licenseIssueDate = licenseIssueDate
        )
        userRepository.registerUser(user)
    }


    // ДОБАВЛЕНО: Обновление изображений профиля
    suspend fun updateProfileImage(userId: Long, imagePath: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            userRepository.updateProfileImage(userId, imagePath)
        }
    }

    suspend fun updateLicenseImage(userId: Long, imagePath: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            userRepository.updateLicenseImage(userId, imagePath)
        }
    }

    suspend fun updatePassportImage(userId: Long, imagePath: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            userRepository.updatePassportImage(userId, imagePath)
        }
    }

    // обычный вход
    suspend fun loginUser(email: String, password: String): Result<UserModel> =
        withContext(Dispatchers.IO) {
            userRepository.loginUser(email, password)
        }

    // выход из аккаунта
    suspend fun logoutUser(userId: Long): Result<Boolean> = withContext(context = Dispatchers.IO) {
        try {
            val context = MyApplication.applicationContext()
            val prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
            prefs.edit { remove(KEY_CURRENT_USER_ID) }

            Result.success(true) // Explicitly return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure if any exception occurs
        }
    }

    // проверка авторизации
    suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        userRepository.getCurrentUser() != null
    }

    suspend fun getCurrentUser(): UserModel? = withContext(Dispatchers.IO) {
        userRepository.getCurrentUser()
    }

    // SharedPreferences методы
    fun saveCurrentUserId(context: Context, userId: Long) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit { putLong(KEY_CURRENT_USER_ID, userId) }
    }


    fun getCurrentUserId(context: Context): Long? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (prefs.contains(KEY_CURRENT_USER_ID)) prefs.getLong(KEY_CURRENT_USER_ID, -1) else null
    }

    fun isFirstLaunch(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_IS_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchCompleted(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit { putBoolean(KEY_IS_FIRST_LAUNCH, false) }
    }
}
package com.kleprer.mobileapp.data.repo

import android.content.Context
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.data.dao.UserDao
import com.kleprer.mobileapp.data.models.UserModel

class UserRepo(
    private val userDao: UserDao,
    private val context: Context
) {

    // зарегистрироваться
    suspend fun registerUser(user: UserModel): Result<Long> {
        return try {
            println("DEBUG: Checking if email exists: ${user.email}")
            val existingCount = userDao.checkEmailExists(user.email)
            println("DEBUG: Email exists count: $existingCount")

            if (existingCount > 0) {
                println("DEBUG: Email ${user.email} already exists in database")
                return Result.failure(Exception(context.getString(R.string.error_email_already_exists)))
            }

            println("DEBUG: Inserting new user: ${user.email}")
            val userId = userDao.insertUser(user)
            println("DEBUG: User inserted with ID: $userId")
            Result.success(userId)
        } catch (e: Exception) {
            println("DEBUG: Registration error: ${e.message}")
            Result.failure(Exception(context.getString(R.string.sign_up_error, e.message)))
        }
    }

    // обычный вход
    suspend fun loginUser(email: String, password: String): Result<UserModel> {
        return try {
            val user = userDao.getUserByEmail(email)

            when {
                user == null -> Result.failure(Exception(context.getString(R.string.user_not_found)))
                user.password != password -> Result.failure(Exception(context.getString(R.string.invalid_password)))
                !user.isActive -> Result.failure(Exception(context.getString(R.string.account_deactivated)))
                else -> {
                    val token = generateToken(user.email)
                    val expiry = System.currentTimeMillis() + (24 * 60 * 60 * 1000)

                    userDao.updateUserTokens(user.id, token, "refresh_$token", expiry, System.currentTimeMillis())
                    val updatedUser = userDao.getUserById(user.id)
                    Result.success(updatedUser!!)
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка входа: ${e.message}"))
        }
    }

    // выход
    suspend fun logoutUser(userId: Long): Result<Boolean> {
        return try {
            userDao.logoutUser(userId, System.currentTimeMillis())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка выхода: ${e.message}"))
        }
    }

    // получение текущего пользователя
    suspend fun getCurrentUser(): UserModel? = userDao.getActiveUser()

    // обновление изображений
    suspend fun updateProfileImage(userId: Long, imagePath: String): Result<Boolean> {
        return try {
            val timestamp = System.currentTimeMillis()
            userDao.updateProfileImage(userId, imagePath, timestamp)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLicenseImage(userId: Long, imagePath: String): Result<Boolean> {
        return try {
            val timestamp = System.currentTimeMillis()
            userDao.updateLicenseImage(userId, imagePath, timestamp)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassportImage(userId: Long, imagePath: String): Result<Boolean> {
        return try {
            val timestamp = System.currentTimeMillis()
            userDao.updatePassportImage(userId, imagePath, timestamp)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    // генерация токенов
    private fun generateToken(email: String): String = "app_token_${email.hashCode()}_${System.currentTimeMillis()}"
    private fun generateGoogleToken(email: String, idToken: String?): String = "google_token_${email.hashCode()}_${idToken?.hashCode() ?: System.currentTimeMillis()}"
}
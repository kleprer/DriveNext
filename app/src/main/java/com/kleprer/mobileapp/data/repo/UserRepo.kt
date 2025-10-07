package com.kleprer.mobileapp.data.repo

import UserModel
import com.kleprer.mobileapp.data.dao.UserDao

class UserRepo(private val userDao: UserDao) {

    // зарегистрироваться
    suspend fun registerUser(user: UserModel): Result<Long> {
        return try {
            if (userDao.checkEmailExists(user.email) > 0) {
                return Result.failure(Exception("Пользователь с таким email уже существует"))
            }
            val userId = userDao.insertUser(user)
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка регистрации: ${e.message}"))
        }
    }

    // обычный вход
    suspend fun loginUser(email: String, password: String): Result<UserModel> {
        return try {
            val user = userDao.getUserByEmail(email)

            when {
                user == null -> Result.failure(Exception("Пользователь не найден"))
                user.password != password -> Result.failure(Exception("Неверный пароль"))
                !user.isActive -> Result.failure(Exception("Аккаунт деактивирован"))
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
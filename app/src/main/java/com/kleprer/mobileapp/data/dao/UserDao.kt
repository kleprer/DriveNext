package com.kleprer.mobileapp.data.dao

import androidx.room.*
import com.kleprer.mobileapp.data.models.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserModel): Long

    // поиск пользователей
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserModel?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserModel?

    @Query("SELECT * FROM users WHERE accessToken = :token AND isActive = 1")
    suspend fun getUserByToken(token: String): UserModel?

    // получение активных пользователей
    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getAllActiveUsers(): Flow<List<UserModel>>

    @Query("SELECT * FROM users WHERE accessToken IS NOT NULL AND isActive = 1 ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getActiveUser(): UserModel?

    // управление токенами
    @Query("UPDATE users SET accessToken = :accessToken, refreshToken = :refreshToken, tokenExpiry = :expiry, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updateUserTokens(userId: Long, accessToken: String, refreshToken: String, expiry: Long, timestamp: Long)

    @Query("UPDATE users SET accessToken = NULL, refreshToken = NULL, tokenExpiry = NULL, updatedAt = :timestamp WHERE id = :userId")
    suspend fun logoutUser(userId: Long, timestamp: Long)

    // валидация
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun checkEmailExists(email: String): Int

    // обновление путей к изображениям
    @Query("UPDATE users SET profileImagePath = :profilePath, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updateProfileImage(userId: Long, profilePath: String, timestamp: Long)

    @Query("UPDATE users SET licenseImagePath = :licensePath, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updateLicenseImage(userId: Long, licensePath: String, timestamp: Long)

    @Query("UPDATE users SET passportImagePath = :passportPath, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updatePassportImage(userId: Long, passportPath: String, timestamp: Long)
}
package com.kleprer.mobileapp.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.kleprer.mobileapp.data.dao.UserDao

@Database(
    entities = [UserModel::class], // Make sure UserModel exists
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() { // Renamed to avoid confusion

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Better database name
                )
                    .fallbackToDestructiveMigration(false) // Remove the (false) parameter
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 4, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UsersDatabase? = null
        fun getInstance(context: Context): UsersDatabase =
            INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UsersDatabase::class.java,
                    "Users.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}
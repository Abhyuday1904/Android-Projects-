package com.example.foodwastagereductionapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodwastagereductionapp.pages.FeedbackDao
import com.example.foodwastagereductionapp.pages.Feedbackk

@Database(entities = [ImagePost::class, Feedbackk::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagePostDao(): ImagePostDao
    abstract fun feedbackDao(): FeedbackDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Use for dev; handle migrations in production
                    .build().also { INSTANCE = it }
            }
        }
    }
}

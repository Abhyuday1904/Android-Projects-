package com.example.foodwastagereductionapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImagePost::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagePostDao(): ImagePostDao
}

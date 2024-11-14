package com.example.foodwastagereductionapp.data

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

@Entity(tableName = "menu")
data class Menu(
    @PrimaryKey val date: String,     // YYYY-MM-DD format
    val day: String,
    val breakfast: String,
    val lunch: String,
    val snacks: String,
    val dinner: String
)



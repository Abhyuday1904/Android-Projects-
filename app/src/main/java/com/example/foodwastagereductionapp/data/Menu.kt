package com.example.foodwastagereductionapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "menu_table")
data class Menu(
    @PrimaryKey val mealType: String,
    val optInCount: Int = 0
)




package com.example.foodwastagereductionapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MenuDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(menus: List<Menu>)

    @Query("SELECT * FROM menu WHERE date = :date")
    suspend fun getMenuForDate(date: String): Menu?
}

package com.example.foodwastagereductionapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_posts")
data class ImagePost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val description: String,
    val timestamp: Long

)

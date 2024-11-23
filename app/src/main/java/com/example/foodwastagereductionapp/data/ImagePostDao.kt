package com.example.foodwastagereductionapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImagePostDao {
    @Insert
    suspend fun insertImagePost(post: ImagePost)

    @Query("SELECT * FROM image_posts ORDER BY timestamp DESC")
    suspend fun getAllImagePosts(): List<ImagePost>
}

package com.nammashale.shalepride.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val imageUrl: String = "",
    val type: String,        // activity, announcement, achievement, meal
    val authorName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

package com.nammashale.shalepride.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meals",
    indices = [Index(value = ["date"], unique = true)]
)
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,        // yyyy-MM-dd format, unique per day
    val menu: String,
    val imageUrl: String,
    val uploadedBy: String,
    val timestamp: Long = System.currentTimeMillis()
)

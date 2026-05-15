package com.nammashale.shalepride.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_stars")
data class StudentStarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val className: String,
    val achievement: String,
    val photoUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

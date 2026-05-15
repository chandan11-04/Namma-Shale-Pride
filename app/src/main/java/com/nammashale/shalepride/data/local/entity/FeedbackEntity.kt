package com.nammashale.shalepride.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class FeedbackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val message: String,
    val isAnonymous: Boolean = false,
    val senderName: String = "",
    val senderEmail: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val sentiment: String = "NEUTRAL"  // Feature 2: AI Sentiment
)

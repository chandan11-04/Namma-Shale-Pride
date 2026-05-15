package com.nammashale.shalepride.data.model

data class Feedback(
    val id: Long = 0,
    val message: String = "",
    val isAnonymous: Boolean = false,
    val senderName: String = "",
    val senderEmail: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val sentiment: String = "NEUTRAL"  // Feature 2: AI Sentiment
)

package com.nammashale.shalepride.data.model

data class Post(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val type: String = "", // activity, announcement, achievement, meal
    val authorName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

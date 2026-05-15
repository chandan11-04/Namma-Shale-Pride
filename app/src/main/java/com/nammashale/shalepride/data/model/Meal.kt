package com.nammashale.shalepride.data.model

data class Meal(
    val id: Long = 0,
    val date: String = "",
    val menu: String = "",
    val imageUrl: String = "",
    val uploadedBy: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

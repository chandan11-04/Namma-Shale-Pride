package com.nammashale.shalepride.data.model

data class StudentStar(
    val id: Long = 0,
    val name: String = "",
    val className: String = "",
    val achievement: String = "",
    val photoUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

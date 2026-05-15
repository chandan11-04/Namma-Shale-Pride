package com.nammashale.shalepride.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "parent" // "admin" or "parent"
)

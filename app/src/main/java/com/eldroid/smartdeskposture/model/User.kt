package com.eldroid.smartdeskposture.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val registrationDate: Long = System.currentTimeMillis()
)

package com.eldroid.smartdeskposture.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val registrationDate: Long = System.currentTimeMillis()
) {
    // Default constructor for Firebase
    constructor() : this("", "", "", 0L)
}

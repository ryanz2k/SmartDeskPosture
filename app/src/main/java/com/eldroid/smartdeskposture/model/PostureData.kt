package com.eldroid.smartdeskposture.model

data class PostureData(
    val id: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val postureType: String = "", // "Good", "Fair", "Poor"
    val duration: Long = 0L, // Duration in minutes
    val reminderCount: Int = 0
)

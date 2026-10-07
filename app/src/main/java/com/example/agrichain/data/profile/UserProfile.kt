package com.example.agrichain.data.profile

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
package com.foodback.demo.model.response.auth

data class AuthResponse(
    val uid: String,
    val email: String,
    val role: String,
    val idToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

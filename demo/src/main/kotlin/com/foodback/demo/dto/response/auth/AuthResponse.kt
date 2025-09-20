package com.foodback.demo.dto.response.auth

data class AuthResponse(
    val uid: String,
    val email: String,
    val roles: List<String>,
    val idToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

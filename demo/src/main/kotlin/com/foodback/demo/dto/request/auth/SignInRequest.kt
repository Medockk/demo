package com.foodback.demo.dto.request.auth

data class SignInRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true,
)
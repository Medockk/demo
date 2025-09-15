package com.foodback.demo.model.request.auth

data class SignInRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true,
)
package com.foodback.demo.model.request.auth

data class SignUpRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)

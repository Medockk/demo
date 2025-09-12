package com.foodback.demo.exception.auth

class UserNotFoundException(
    private val email: String
): RuntimeException(
    "User with email $email not found"
)
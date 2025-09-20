package com.foodback.demo.exception.auth

class UserNotFoundException(
    email: String
): RuntimeException(
    "User with email $email not found"
)
package com.foodback.demo.exception.auth

/**
 * User not found Exception with special [email]
 */
class UserNotFoundException(
    email: String
): RuntimeException(
    "User with email $email not found"
)
package com.foodback.demo.exception.auth

/**
 * Token verification failed Exception with special [message]
 */
class TokenVerificationFailedException(message: String)
    : RuntimeException(message)
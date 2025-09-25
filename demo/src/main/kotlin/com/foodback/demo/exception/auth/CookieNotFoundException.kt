package com.foodback.demo.exception.auth

/**
 * Cookie not found Exception with special [message]
 */
class CookieNotFoundException(errorMessage: String): RuntimeException(errorMessage)
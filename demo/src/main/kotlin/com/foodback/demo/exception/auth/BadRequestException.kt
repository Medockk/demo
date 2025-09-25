package com.foodback.demo.exception.auth

/**
 * Bad Request Exception with special [message]
 */
class BadRequestException(message: String): RuntimeException(message)
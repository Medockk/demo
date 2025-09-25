package com.foodback.demo.dto.response.auth

/**
 * Response from Authenticate User
 * @param uid Unique user identifier
 * @param email Email of current user
 * @param roles The List of roles of the current user
 * @param refreshToken Special Token to refresh JWT token
 * @param expiresIn How long JWT token will expire
 */
data class AuthResponse(
    val uid: String,
    val email: String,
    val roles: List<String>,
    val jwtToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

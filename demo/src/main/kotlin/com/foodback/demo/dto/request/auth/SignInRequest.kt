package com.foodback.demo.dto.request.auth

/**
 * Request to Authorize User
 * @param email Email of user
 * @param password Password of user
 * @param returnSecureToken Not required param. Should user get secure token or not
 */
data class SignInRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean? = true,
)
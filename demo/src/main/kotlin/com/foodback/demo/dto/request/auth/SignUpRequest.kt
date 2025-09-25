package com.foodback.demo.dto.request.auth

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Request to Register new user
 * @param email Email of user
 * @param password Password of user
 * @param displayName Not required param. User name
 * @param returnSecureToken Not required param. Should user get secure token or not
 */
data class SignUpRequest(
    val email: String,
    val password: String,
    @JsonProperty(required = false)
    val displayName: String? = null,
    val returnSecureToken: Boolean = true,
)

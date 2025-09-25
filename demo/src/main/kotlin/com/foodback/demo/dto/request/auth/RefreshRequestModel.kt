package com.foodback.demo.dto.request.auth

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Request to refresh JWT Token
 * @param refreshToken Refresh token to get new JWT token
 * @param grantType Not required param to set what type of token we want
 */
data class RefreshRequestModel(
    @JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("grant_type") val grantType: String? = "refresh_token"
)

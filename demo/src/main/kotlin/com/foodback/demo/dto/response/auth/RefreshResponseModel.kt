package com.foodback.demo.dto.response.auth

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response from refresh request
 * @param jwtToken New JWT token
 * @param expiresIn Parameter to show, how long new JWT token will expire
 */
data class RefreshResponseModel(
    @JsonProperty("id_token") val jwtToken: String,
    //@JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
//    @JsonProperty("user_id") val userId: String,
//    @JsonProperty("project_id") val projectId: String,
//    @JsonProperty("token_type") val tokenType: String,
//    @JsonProperty("access_token") val accessToken: String
)

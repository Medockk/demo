package com.foodback.demo.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshRequestModel(
    @JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("grant_type") val grantType: String = "refresh_token"
)

data class RefreshResponseModel(
    @JsonProperty("id_token") val idToken: String,
    @JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
    @JsonProperty("user_id") val userId: String,
    @JsonProperty("project_id") val projectId: String,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("access_token") val accessToken: String
)


package com.foodback.demo.dto.request.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshRequestModel(
    @JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("grant_type") val grantType: String = "refresh_token"
)

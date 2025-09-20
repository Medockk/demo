package com.foodback.demo.dto.request.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class SignUpRequest(
    val email: String,
    val password: String,
    @JsonProperty(required = false)
    val displayName: String? = null,
    val returnSecureToken: Boolean = true,
)

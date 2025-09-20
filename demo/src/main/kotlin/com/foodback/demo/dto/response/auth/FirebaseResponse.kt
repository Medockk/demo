package com.foodback.demo.dto.response.auth

import com.foodback.demo.entity.Roles
import com.foodback.demo.entity.UserEntity

data class FirebaseResponse(
    val idToken: String,
    val refreshToken: String,
    val expiresIn: String,
    val localId: String,
    val email: String?,
    val name: String?,
    val displayName: String?,
    val photoUrl: String? = null
){
    fun toAuthResponse() =
        AuthResponse(
            uid = localId,
            email = email ?: "",
            roles = listOf(Roles.USER.name),
            idToken = idToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn.toLongOrNull() ?: 3600L
        )

    fun toUserEntity() =
        UserEntity(
            uid = localId,
            email = email ?: "",
            name = displayName,
            roles = mutableListOf(Roles.USER.name),
            photoUrl = photoUrl
        )
}

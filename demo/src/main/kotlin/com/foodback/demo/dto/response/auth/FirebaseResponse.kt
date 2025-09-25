package com.foodback.demo.dto.response.auth

import com.foodback.demo.entity.Roles
import com.foodback.demo.entity.UserEntity

/**
 * Special response from FirebaseAuth
 * @param localId Unique user identifier
 * @param idToken JWT token of current user
 * @param refreshToken Refresh token to update JWT token
 * @param expiresIn How long JWT token will expire
 * @param email Email of user
 * @param displayName Username
 * @param photoUrl Url to user avatar
 */
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
    /**
     * Map [com.foodback.demo.dto.response.auth.FirebaseResponse] to [AuthResponse]
     */
    fun toAuthResponse() =
        AuthResponse(
            uid = localId,
            email = email ?: "",
            roles = listOf(Roles.USER.name),
            jwtToken = idToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn.toLongOrNull() ?: 3600L
        )

    /**
     * Map [com.foodback.demo.dto.response.auth.FirebaseResponse] to [UserEntity]
     */
    fun toUserEntity() =
        UserEntity(
            uid = localId,
            email = email ?: "",
            name = displayName,
            roles = mutableListOf(Roles.USER.name),
            photoUrl = photoUrl
        )
}

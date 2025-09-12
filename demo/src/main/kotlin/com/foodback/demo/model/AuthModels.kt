package com.foodback.demo.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

data class SignInRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true,
)
data class SignUpRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)

data class AuthResponse(
    val uid: String,
    val email: String,
    val role: String,
    val idToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    var uid: String = "",
    @Column(nullable = false)
    var email: String = "",
    @Column(nullable = true)
    var name: String? = "",
    var role: String = "ROLE_USER",
    @Column(nullable = true)
    var photoUrl: String? = null,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
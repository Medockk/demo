package com.foodback.demo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

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

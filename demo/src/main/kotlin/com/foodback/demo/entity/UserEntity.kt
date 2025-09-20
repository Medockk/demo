package com.foodback.demo.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
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
    @Column(nullable = true)
    var photoUrl: String? = null,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "uid")],
        foreignKey = ForeignKey(ConstraintMode.CONSTRAINT)
    )
    @Column(name = "role")
    var roles: MutableList<String> = mutableListOf(Roles.USER.name)
)

package com.foodback.demo.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

data class HelloModelDto(
    var id: Long = 0,
    var content: String = "",
    var createdAt: Instant? = null
){
    fun toEntity() =
        HelloModelEntity(id, content, createdAt)
}

@Entity
@Table(
    name = "Hello"
)
data class HelloModelEntity(
    @Id
    @GeneratedValue(GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(nullable = false)
    var content: String = "",
    @CreationTimestamp
    var createdAt: Instant? = Instant.now()
) {
    fun toDto() =
        HelloModelDto(id, content, createdAt)
}

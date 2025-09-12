package com.foodback.demo.repository

import com.foodback.demo.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, String> {

    fun findByEmail(email: String): UserEntity?
}
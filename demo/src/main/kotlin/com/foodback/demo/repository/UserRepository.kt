package com.foodback.demo.repository

import com.foodback.demo.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Special Repository to make requests to Database with JpaRepository.
 */
interface UserRepository: JpaRepository<UserEntity, String> {

    /**
     * Method to find user by email
     * @param email Email of user to find in database
     * @return A [UserEntity] of this user or null, if user not found
     */
    fun findByEmail(email: String): UserEntity?
}
package com.foodback.demo.entity

/**
 * Enum class with all roles.
 * @param permissions Permissions of current role
 */
enum class Roles(val permissions: Set<String>) {
    USER(setOf(Permissions.REFRESH_TOKEN.value)),
    ADMIN(setOf(Permissions.GET_USERS.value, Permissions.REFRESH_TOKEN.value))
}

/**
 * Enum class with all permissions
 */
enum class Permissions(val value: String) {
    REFRESH_TOKEN("REFRESH_TOKEN"),
    GET_USERS("GET_USERS"),
}
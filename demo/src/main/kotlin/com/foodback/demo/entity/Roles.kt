package com.foodback.demo.entity

enum class Roles(val permissions: Set<String>) {

    USER(setOf(Permissions.REFRESH_TOKEN.value)),
    ADMIN(setOf(Permissions.GET_USERS.value, Permissions.REFRESH_TOKEN.value))
}

enum class Permissions(val value: String) {

    REFRESH_TOKEN("REFRESH_TOKEN"),
    GET_USERS("GET_USERS"),
}
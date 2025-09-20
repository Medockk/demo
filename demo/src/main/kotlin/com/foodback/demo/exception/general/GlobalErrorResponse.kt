package com.foodback.demo.exception.general

data class GlobalErrorResponse(
    val error: String,
    val message: String,
    val code: Int,
    val timestamp: Long = System.currentTimeMillis(),
)

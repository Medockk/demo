package com.foodback.demo.exception.handler

import com.foodback.demo.exception.auth.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onUserNotFound(e: UserNotFoundException) = mapOf(
        "errorCode" to "USER_NOT_FOUND",
        "message" to e.message
    )
}
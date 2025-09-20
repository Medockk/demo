package com.foodback.demo.exception.handler

import com.foodback.demo.exception.general.GlobalErrorResponse
import com.foodback.demo.exception.auth.BadRequestException
import com.foodback.demo.exception.auth.CookieNotFoundException
import com.foodback.demo.exception.auth.TokenVerificationFailedException
import com.foodback.demo.exception.auth.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class AuthExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onUserNotFound(e: UserNotFoundException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                GlobalErrorResponse(
                    error = "User not found!",
                    message = e.message ?: e.localizedMessage ?: "Oops...user not found",
                    code = HttpStatus.NOT_FOUND.value()
                )
            )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(exception: BadRequestException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                GlobalErrorResponse(
                    error = "Bad Request Exception",
                    message = exception.message ?: "Unknown exception...",
                    code = HttpStatus.BAD_REQUEST.value()
                )
            )
    }
    @ExceptionHandler(CookieNotFoundException::class)
    fun handleCookieNotFoundException(exception: CookieNotFoundException):
            ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                GlobalErrorResponse(
                    error = "Cookie not found exception",
                    message = exception.message ?: "Unknown exception...",
                    code = HttpStatus.BAD_REQUEST.value()
                )
            )
    }
    @ExceptionHandler(TokenVerificationFailedException::class)
    fun handleTokenVerificationFailed(exception: TokenVerificationFailedException):
            ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                GlobalErrorResponse(
                    error = "Token verification failed",
                    message = exception.message ?: "Unknown exception...",
                    code = HttpStatus.UNAUTHORIZED.value()
                )
            )
    }
}
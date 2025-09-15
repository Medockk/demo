package com.foodback.demo.exception.handler

import com.foodback.demo.exception.GlobalErrorResponse
import com.foodback.demo.exception.auth.BadRequestException
import com.foodback.demo.exception.auth.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.SocketException
import java.util.concurrent.TimeoutException

@RestControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onUserNotFound(e: UserNotFoundException) = mapOf(
        "errorCode" to "USER_NOT_FOUND",
        "message" to e.message
    )

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

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(exception: RuntimeException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                GlobalErrorResponse(
                    error = "Runtime Exception",
                    message = exception.message ?: "Unknown exception...",
                    code = HttpStatus.BAD_REQUEST.value()
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(exception: Exception): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                GlobalErrorResponse(
                    error = "Internal server error",
                    message = exception.message ?: "Unknown exception...",
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            )
    }

    @ExceptionHandler(SocketException::class)
    fun handleSocketException(exception: SocketException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(
                GlobalErrorResponse(
                    error = "Network Exception",
                    message = "Network error when accessing a remote service: ${exception.message}",
                    code = HttpStatus.BAD_GATEWAY.value()
                )
            )
    }
    @ExceptionHandler(TimeoutException::class)
    fun handleTimeoutException(exception: TimeoutException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.GATEWAY_TIMEOUT)
            .body(
                GlobalErrorResponse(
                    error = "Timeout Exception",
                    message = "Oops...server throw timeout exception with message: ${exception.message}",
                    code = HttpStatus.GATEWAY_TIMEOUT.value()
                )
            )
    }
}
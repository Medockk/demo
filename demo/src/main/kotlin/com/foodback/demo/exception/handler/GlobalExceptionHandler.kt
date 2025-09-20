package com.foodback.demo.exception.handler

import com.foodback.demo.exception.general.GlobalErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeoutException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(exception: RuntimeException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                GlobalErrorResponse(
                    error = "Runtime Exception",
                    message = exception.message ?: "Unknown exception...",
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value()
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
    @ExceptionHandler(IOException::class)
    fun handleIOException(exception: IOException): ResponseEntity<GlobalErrorResponse> {
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
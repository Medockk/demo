package com.foodback.demo.controller

import com.foodback.demo.dto.request.auth.RefreshRequestModel
import com.foodback.demo.dto.request.auth.SignInRequest
import com.foodback.demo.dto.request.auth.SignUpRequest
import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.dto.response.auth.RefreshResponseModel
import com.foodback.demo.entity.UserEntity
import com.foodback.demo.service.FirebaseAuthService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val firebaseAuthService: FirebaseAuthService
) {

    @PostMapping("signUp")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val response = firebaseAuthService.signUp(signUpRequest, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    @PostMapping("signIn")
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val response = firebaseAuthService.signIn(signInRequest, response)
        return ResponseEntity.ok(response)
    }

    @PostMapping("refresh")
    fun refreshToken(
        @RequestBody refreshRequestModel: RefreshRequestModel,
        response: HttpServletResponse
    ): ResponseEntity<RefreshResponseModel> {
        val response = firebaseAuthService.refreshToken(refreshRequestModel, response)
        return ResponseEntity.ok(response)
    }
}

@RestController
@RequestMapping("/api/users")
class UsersController(
    val firebaseAuthService: FirebaseAuthService
) {

    @GetMapping("role/user")
    @PreAuthorize("hasRole('USER')")
    fun getUserUsers(): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("with role = USER you don't have this permission")
    }

    @GetMapping("role/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAdminUsers(): ResponseEntity<List<UserEntity>> {
        return try {
            ResponseEntity.ok(firebaseAuthService.getUsers())
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }
    @GetMapping("getusers")
    @PreAuthorize("hasAuthority('GET_USERS')")
    fun g(): ResponseEntity<List<UserEntity>> {
        return try {
            ResponseEntity.ok(firebaseAuthService.getUsers())
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }
}
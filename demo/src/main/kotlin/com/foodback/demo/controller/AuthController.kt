package com.foodback.demo.controller

import com.foodback.demo.model.AuthResponse
import com.foodback.demo.model.RefreshRequestModel
import com.foodback.demo.model.RefreshResponseModel
import com.foodback.demo.model.SignInRequest
import com.foodback.demo.model.SignUpRequest
import com.foodback.demo.service.FirebaseAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.coroutines.coroutineContext

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val firebaseAuthService: FirebaseAuthService
) {

    @PostMapping("signUp")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<AuthResponse> {
        val response = firebaseAuthService.registerUser(signUpRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("signIn")
    fun signIn(
        @RequestBody signInRequest: SignInRequest
    ): ResponseEntity<AuthResponse> {
        val response = firebaseAuthService.signInWithEmailAndPassword(signInRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("refresh")
    fun refreshToken(
        @RequestBody refreshRequestModel: RefreshRequestModel
    ): ResponseEntity<RefreshResponseModel> {
        val response = firebaseAuthService.refreshToken(refreshRequestModel)
        return ResponseEntity.ok(response)
    }
}
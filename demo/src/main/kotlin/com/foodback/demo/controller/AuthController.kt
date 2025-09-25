package com.foodback.demo.controller

import com.foodback.demo.dto.request.auth.RefreshRequestModel
import com.foodback.demo.dto.request.auth.SignInRequest
import com.foodback.demo.dto.request.auth.SignUpRequest
import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.dto.response.auth.RefreshResponseModel
import com.foodback.demo.entity.UserEntity
import com.foodback.demo.service.FirebaseAuthService
import com.foodback.demo.service.ImageService
import com.google.firebase.auth.FirebaseAuth
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Paths

/**
 * Controller to authenticate users and update jwt token
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val firebaseAuthService: FirebaseAuthService
) {

    /**
     * Method to register user
     * @param signUpRequest Request body to register user.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [AuthResponse] Response of registration.
     * @throws com.foodback.demo.exception.auth.BadRequestException if arguments wrong or illegal,
     * @throws com.google.firebase.auth.FirebaseAuthException if user already registered or wrong user data,
     * @throws RuntimeException server exception,
     * @throws java.net.SocketException connection exception.
     */
    @PostMapping("signUp")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val response = firebaseAuthService.signUp(signUpRequest, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to login user
     * @param signInRequest Request body to register user.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [AuthResponse] - Response of authentication.
     * @throws com.foodback.demo.exception.auth.BadRequestException if arguments wrong or illegal,
     * @throws com.google.firebase.auth.FirebaseAuthException if user already registered or wrong user data,
     * @throws RuntimeException server exception,
     * @throws java.net.SocketException connection exception.
     */
    @PostMapping("signIn")
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val response = firebaseAuthService.signIn(signInRequest, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to refresh jwt token
     * @param refreshRequestModel Request body to refresh jwt token.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [RefreshResponseModel] - Response of refresh jwt token.
     * @throws com.foodback.demo.exception.auth.BadRequestException if arguments wrong or illegal,
     * @throws com.google.firebase.auth.FirebaseAuthException if refresh token invalid,
     * @throws RuntimeException server exception,
     * @throws java.net.SocketException connection exception.
     */
    @PostMapping("refresh")
    fun refreshToken(
        @RequestBody refreshRequestModel: RefreshRequestModel,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<RefreshResponseModel> {
        val response = firebaseAuthService.refreshToken(refreshRequestModel, httpServletResponse)
        return ResponseEntity.ok(response)
    }
}

@RestController
@RequestMapping("/api/users")
class UsersController(
    val firebaseAuthService: FirebaseAuthService,
    private val supabaseService: ImageService
) {

    @PostMapping("image")
    fun uploadImage(
        @RequestParam("file") file: MultipartFile,
        httpServletRequest: HttpServletRequest
    ) {
        if (file.isEmpty) {
            throw Exception("File is empty!")
        }

//        val directory = System.getProperty("user.dir") + "/spring_images"
//        File(directory).mkdirs()
        val time = System.currentTimeMillis()

        val name = file.originalFilename ?: file.name
        val originName = name.dropLastWhile { it != '.' }.dropLast(1)
        val suffix = name.replaceBeforeLast(
            ".",
            "_$time"
        )
//        try {
//            val bytes = file.bytes
//            val path = Paths.get(
//                "$directory/$originName$suffix"
//            )
//            Files.write(path, bytes)
//            path.toFile().setLastModified(time)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            throw e
//        }

        val jwt = httpServletRequest.cookies.find { it.name == "jwt" }?.value ?: return
        val uid = FirebaseAuth.getInstance().verifyIdToken(jwt).uid
        firebaseAuthService.getUsers()
        supabaseService.uploadAvatar(file, "$originName$suffix", uid)
    }

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
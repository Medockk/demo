package com.foodback.demo.service

import com.foodback.demo.dto.request.auth.RefreshRequestModel
import com.foodback.demo.dto.request.auth.SignInRequest
import com.foodback.demo.dto.request.auth.SignUpRequest
import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.dto.response.auth.FirebaseResponse
import com.foodback.demo.dto.response.auth.RefreshResponseModel
import com.foodback.demo.entity.Roles
import com.foodback.demo.entity.UserEntity
import com.foodback.demo.exception.auth.BadRequestException
import com.foodback.demo.repository.UserRepository
import com.foodback.demo.utils.CookieUtil
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class FirebaseAuthService(
    @Value($$"${firebase.api-key}")
    private val apiKey: String,
    private val userRepository: UserRepository,
    private val cookieUtil: CookieUtil
) {

    fun getUsers(): List<UserEntity> {
        return userRepository.findAll()
    }

    private val client = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

    fun signUp(
        signUpRequest: SignUpRequest,
        httpServletResponse: HttpServletResponse
    ): AuthResponse {
        val response = client
            .baseUrl("https://identitytoolkit.googleapis.com")
            .build()
            .post()
            .uri("/v1/accounts:signUp?key=$apiKey")
            .bodyValue(signUpRequest)
            .retrieve()
            .onStatus({ it.isError }) {
                it.bodyToMono(String::class.java)
                    .flatMap { cause ->
                        Mono.error(BadRequestException("Firebase exception is $cause"))
                    }
            }
            .bodyToMono(FirebaseResponse::class.java)
            .retry(3)
            .block()!!

        val cookie = cookieUtil.createJwtCookie(response.idToken)
        httpServletResponse.addCookie(cookie)

        val entity = response.toUserEntity().apply {
            roles = mutableListOf(Roles.USER.name)
        }
        val user = userRepository.save(entity)

        return response.toAuthResponse().copy(
            roles = user.roles
        )
    }

    fun signIn(
        signInRequest: SignInRequest,
        httpServletResponse: HttpServletResponse
    ): AuthResponse {
        val response = client
            .baseUrl("https://identitytoolkit.googleapis.com")
            .build()
            .post()
            .uri("/v1/accounts:signInWithPassword?key=$apiKey")
            .bodyValue(signInRequest)
            .retrieve()
            .onStatus({ it.is4xxClientError }) {
                it.bodyToMono(String::class.java)
                    .flatMap{ response ->
                        Mono.error(BadRequestException(response))
                }
            }.onStatus({ it.is5xxServerError }) {
                it.bodyToMono(FirebaseAuthException::class.java)
                    .flatMap{ response ->
                        Mono.error(RuntimeException(response))
                }
            }.onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java)
                    .flatMap {
                        Mono.error(BadRequestException("Firebase Exception is $it"))
                    }
            }.bodyToMono(FirebaseResponse::class.java)
            .block()!!

        val cookie = cookieUtil.createJwtCookie(response.idToken)
        httpServletResponse.addCookie(cookie)

        val user = userRepository.findByEmail(signInRequest.email)

        return response.toAuthResponse().copy(
            roles = user?.roles ?: listOf(Roles.USER.name)
        )
    }

    fun refreshToken(
        requestModel: RefreshRequestModel,
        httpServletResponse: HttpServletResponse
    ): RefreshResponseModel {
        val response = client
            .baseUrl("https://securetoken.googleapis.com")
            .build()
            .post()
            .uri("/v1/token?key=$apiKey")
            .body(
                /* inserter = */ BodyInserters.fromFormData("grant_type", "refresh_token")
                    .with("refresh_token", requestModel.refreshToken)
            )
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java)
                    .flatMap {
                        Mono.error(BadRequestException("Token Exception is $it"))
                    }
            }
            .bodyToMono(RefreshResponseModel::class.java)
            .block()!!

        val cookie = cookieUtil.createJwtCookie(response.idToken)
        httpServletResponse.addCookie(cookie)

        return response
    }
}
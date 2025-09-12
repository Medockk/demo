package com.foodback.demo.service

import com.foodback.demo.model.*
import com.foodback.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val ROLE_USER = "ROLE_USER"
private const val ROLE_ADMIN = "ROLE_ADMIN"


@Service
class FirebaseAuthService(
    @Value($$"${firebase.api-key}")
    private val apiKey: String,
    private val userRepository: UserRepository
) {

    private val client = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

    fun registerUser(signUpRequest: SignUpRequest): AuthResponse {
        val response = client
            .baseUrl("https://identitytoolkit.googleapis.com")
            .build()
            .post()
            .uri("/v1/accounts:signUp?key=$apiKey")
            .bodyValue(signUpRequest)
            .retrieve()
            .bodyToMono(FirebaseResponse::class.java)
            .block()!!

        val entity = response.toUserEntity().apply {
            role = ROLE_USER
        }
        userRepository.save(entity)

        return response.toAuthResponse().copy(
            role = entity.role
        )
    }

    fun signInWithEmailAndPassword(signInRequest: SignInRequest): AuthResponse {
        val response = client
            .baseUrl("https://identitytoolkit.googleapis.com")
            .build()
            .post()
            .uri("/v1/accounts:signInWithPassword?key=$apiKey")
            .bodyValue(signInRequest)
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java)
                    .flatMap {
                        Mono.error(RuntimeException("Firebase exception $it"))
                    }
            }
            .bodyToMono(FirebaseResponse::class.java)
            .block()!!

        val user = userRepository.findByEmail(signInRequest.email)

        return response.toAuthResponse().copy(
            role = user?.role ?: ROLE_USER
        )
    }

    fun refreshToken(requestModel: RefreshRequestModel): RefreshResponseModel {
        val response = client
            .baseUrl("https://securetoken.googleapis.com")
            .build()
            .post()
            .uri("/v1/token?key=$apiKey")
            .body(
                BodyInserters.fromFormData("grant_type", "refresh_token")
                    .with("refresh_token", requestModel.refreshToken)
            )
            .retrieve()
            .onStatus({ it.isError }) {
                it.bodyToMono(String::class.java).flatMap { cause ->
                    Mono.error(RuntimeException("Token exception $cause"))
                }
            }
            .bodyToMono(RefreshResponseModel::class.java)
            .block()!!

        return response
    }

    private data class FirebaseResponse(
        val idToken: String,
        val refreshToken: String,
        val expiresIn: String,
        val localId: String,
        val email: String?,
        val name: String?,
        val displayName: String?,
        val photoUrl: String? = null
    ){
        fun toAuthResponse() =
            AuthResponse(
                uid = localId,
                email = email ?: "",
                role = "",
                idToken = idToken,
                refreshToken = refreshToken,
                expiresIn = expiresIn.toLongOrNull() ?: 3600L
            )
        
        fun toUserEntity() =
            UserEntity(
                uid = localId,
                email = email ?: "",
                name = displayName,
                role = "",
                photoUrl = photoUrl
            )
    }
}
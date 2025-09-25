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
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * Service to Authenticate user and update JWT token
 * @param apiKey Special Firebase Api Token to make requests to IdentityToolKit and SecureToken services
 * @param userRepository Repository to connect and make requests to database
 * @param webClient Special client to make requests to special services
 * @param cookieUtil Utility to put jwt token to Cookie
 */
@Service
class FirebaseAuthService(
    @Value($$"${firebase.api-key}")
    private val apiKey: String,
    private val userRepository: UserRepository,
    private val webClient: WebClient,
    private val cookieUtil: CookieUtil,
) {

    fun getUsers(): List<UserEntity> {
        return userRepository.findAll()
    }

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
    fun signUp(
        signUpRequest: SignUpRequest,
        httpServletResponse: HttpServletResponse
    ): AuthResponse {
        val response = postToFirebase(
            uri = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=$apiKey",
            body = signUpRequest,
            responseType = FirebaseResponse::class.java
        )
        httpServletResponse.addJwtCookie(response.idToken)

        val entity = response.toUserEntity().apply {
            roles = mutableListOf(Roles.USER.name)
        }
        val user = userRepository.save(entity)
        return response.toAuthResponse().copy(
            roles = user.roles
        )
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
    fun signIn(
        signInRequest: SignInRequest,
        httpServletResponse: HttpServletResponse
    ): AuthResponse {
        val response = postToFirebase(
            uri = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$apiKey",
            body = signInRequest,
            responseType = FirebaseResponse::class.java
        )
        httpServletResponse.addJwtCookie(response.idToken)

        val user = userRepository.findByEmail(signInRequest.email)
        return response.toAuthResponse().copy(
            roles = user?.roles ?: listOf(Roles.USER.name)
        )
    }

    /**
     * Method to refresh jwt token
     * @param requestModel Request body to refresh jwt token.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [RefreshResponseModel] - Response of refresh jwt token.
     * @throws com.foodback.demo.exception.auth.BadRequestException if arguments wrong or illegal,
     * @throws com.google.firebase.auth.FirebaseAuthException if refresh token invalid,
     * @throws RuntimeException server exception,
     * @throws java.net.SocketException connection exception.
     */
    fun refreshToken(
        requestModel: RefreshRequestModel,
        httpServletResponse: HttpServletResponse
    ): RefreshResponseModel {
        val response = postToFirebase(
            uri = "https://securetoken.googleapis.com/v1/token?key=$apiKey",
            body = mapOf(
                "grant_type" to "refresh_token",
                "refresh_token" to requestModel.refreshToken
            ),
            responseType = RefreshResponseModel::class.java
        )
        httpServletResponse.addJwtCookie(response.jwtToken)

        return response
    }

    /**
     * Method to make HTTP request to special [uri]
     * @param uri URI that make HTTP request
     * @param body HTTP-body of current request
     * @param responseType Type of response
     * @throws BadRequestException if request have invalid arguments
     * @throws RuntimeException if current request caused exception from server side
     */
    private fun <T : Any> postToFirebase(
        uri: String,
        body: Any,
        responseType: Class<T>
    ): T {
        return webClient.post()
            .uri(uri)
            .bodyValue(body)
            .retrieve()
            .onStatus({ it.is4xxClientError }) {
                it.bodyToMono(String::class.java).flatMap { cause ->
                    Mono.error(BadRequestException(cause))
                }
            }.onStatus({ it.is5xxServerError }) {
                it.bodyToMono(String::class.java).flatMap { cause ->
                    Mono.error(RuntimeException(cause))
                }
            }.bodyToMono(responseType)
            .retry(3)
            .block()!!
    }

    /**
     * Method to make HTTP request to special [uri]
     * @param uri URI that make HTTP request
     * @param body HTTP-body of current request
     * @param responseType Type of response
     * @throws BadRequestException if request have invalid arguments
     * @throws RuntimeException if current request caused exception from server side
     */
    private fun <T : Any> postToFirebase(
        uri: String,
        body: Map<String, String>,
        responseType: Class<T>
    ): T {
        return webClient.post()
            .uri(uri)
            .body(BodyInserters.fromFormData(body.toMultiValueMap()))
            .retrieve()
            .onStatus({ it.is4xxClientError }) {
                it.bodyToMono(String::class.java).flatMap { cause ->
                    Mono.error(BadRequestException(cause))
                }
            }.onStatus({ it.is5xxServerError }) {
                it.bodyToMono(String::class.java).flatMap { cause ->
                    Mono.error(RuntimeException(cause))
                }
            }.bodyToMono(responseType)
            .retry(3)
            .block()!!
    }

    /**
     * Method to convert [Map]<[String], [String]> to [MultiValueMap]<[String], [String]>
     */
    private fun Map<String, String>.toMultiValueMap(): MultiValueMap<String, String> {
        val map = LinkedMultiValueMap<String, String>()
        this.forEach { (key, value) -> map.add(key, value) }
        return map
    }

    /**
     * Method to create and add JWT token to cookie
     * @param jwt JWT Token
     */
    private fun HttpServletResponse.addJwtCookie(jwt: String) {
        val cookie = cookieUtil.createJwtCookie(jwt)
        addCookie(cookie)
    }
}
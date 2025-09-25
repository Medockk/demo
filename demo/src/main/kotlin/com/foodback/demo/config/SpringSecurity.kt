package com.foodback.demo.config

import com.foodback.demo.exception.handler.CustomAccessDeniedHandler
import com.foodback.demo.exception.handler.CustomAuthenticationEntryPoint
import com.foodback.demo.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.reactive.function.client.WebClient

/**
 * Security class to configure permissions and main security configurations
 */
@Configuration
class SecurityConfig(
    private val userRepository: UserRepository
) {

    /**
     * Method to provide Single instance of FirebaseAuthFilter
     */
    @Bean
    fun firebaseAuthFilter(): FirebaseAuthFilter {
        return FirebaseAuthFilter(userRepository)
    }

    /**
     * Method to provide Single instance of WebClient
     */
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

    /**
     * Method to authorize HTTP requests for all users or some roles,
     * add FirebaseAuthFilter before [UsernamePasswordAuthenticationFilter],
     * disable csrf check, set session creation policy,
     * and add Exception handler for 401 and 403 HTTP-error
     */
    @Bean
    fun filterChain(
        http: HttpSecurity,
        firebaseAuthFilter: FirebaseAuthFilter,
        authHandler: CustomAuthenticationEntryPoint,
        accessDeniedHandler: CustomAccessDeniedHandler
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(authHandler)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
            }
            .addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
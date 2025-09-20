package com.foodback.demo.config

import com.foodback.demo.exception.handler.CustomAccessDeniedHandler
import com.foodback.demo.exception.handler.CustomAuthenticationEntryPoint
import com.foodback.demo.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val userRepository: UserRepository
) {

    @Bean
    fun firebaseAuthFilter(): FirebaseAuthFilter {
        return FirebaseAuthFilter(userRepository)
    }

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
package com.foodback.demo.config

import com.foodback.demo.entity.Roles
import com.foodback.demo.exception.auth.UserNotFoundException
import com.foodback.demo.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class FirebaseAuthFilter(
    private val userRepository: UserRepository
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return path.startsWith("/api/auth")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = exactToken(request) ?: run {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header")
            return
        }

        val idToken = header.removePrefix("Bearer ").trim()

        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)

            val user = userRepository.findByEmail(decodedToken.email)
                ?: throw UserNotFoundException(decodedToken.email)

            val roles = user
                .roles
                .map {
                    SimpleGrantedAuthority(
                        if (it.startsWith("ROLE_")) it
                        else "ROLE_$it"
                    )
                }
            println(roles)

            val permissions = user
                .roles
                .flatMap { Roles.valueOf(it).permissions }
                .map { SimpleGrantedAuthority(it) }

            val auth = UsernamePasswordAuthenticationToken(
                decodedToken.uid,
                null,
                roles + permissions
            )
            println(auth)
            SecurityContextHolder.getContext().authentication = auth
            request.setAttribute("uid", decodedToken.uid)

            filterChain.doFilter(request, response)
        } catch (e: FirebaseAuthException) {
            throw e
        } catch (e: UserNotFoundException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    private fun exactToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }

        val cookie = request.cookies ?: return null
        return cookie.firstOrNull { it.name == "jwt" }?.value
    }
}
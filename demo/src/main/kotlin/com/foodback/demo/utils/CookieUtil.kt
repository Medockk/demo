package com.foodback.demo.utils

import com.foodback.demo.exception.auth.CookieNotFoundException
import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component

/**
 * Utility to create JWT Token and exact JWT token from Cookies
 */
@Component
class CookieUtil {

    fun createJwtCookie(jwt: String, name: String = "jwt"): Cookie {
        return Cookie(name, jwt).apply {
            path = "/"
            maxAge = 3600
            isHttpOnly = true
            secure = false
        }
    }

    fun getJwtFromCookies(cookie: Array<Cookie>): String {
        return cookie.firstOrNull { it.name == "jwt" }?.value
            ?: throw CookieNotFoundException("Cookie not found")
    }
}
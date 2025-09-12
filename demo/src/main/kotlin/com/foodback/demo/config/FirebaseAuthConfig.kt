package com.foodback.demo.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseAuthConfig {

    @PostConstruct
    fun init() {
        val serviceAccount = this::class.java
            .classLoader
            .getResourceAsStream("firebase-admin-sdk.json")

        val option = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(option)
        }
    }
}
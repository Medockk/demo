package com.foodback.demo.service

import com.foodback.demo.exception.auth.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class ImageService(
    private val webClient: WebClient,
    @Value($$"${supabase.url}")
    private val supabaseUrl: String,
    @Value($$"${supabase.key}")
    private val supabaseKey: String,
) {

    fun uploadAvatar(
        file: MultipartFile,
        imageName: String,
        uid: String,
    ) {

        val requestBody = file.bytes
        try {
            webClient
                .put()
                .uri("$supabaseUrl/storage/v1/object/avatars/$uid/$imageName")
                .header("Authorization", "Bearer $supabaseKey")
                .header("apiKey", supabaseKey)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus({it.is4xxClientError}) {
                    it.bodyToMono(String::class.java).flatMap { cause ->
                        Mono.error(BadRequestException(cause))
                    }
                }
                .onStatus({it.is5xxServerError}) {
                    it.bodyToMono(String::class.java).flatMap { cause ->
                        Mono.error(RuntimeException(cause))
                    }
                }
                .bodyToMono(String::class.java)
                .retry(3)
                .block()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
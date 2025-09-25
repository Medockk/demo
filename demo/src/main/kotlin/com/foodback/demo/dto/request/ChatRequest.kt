package com.foodback.demo.dto.request

/**
 * Request to send message
 * @param senderId Identifier of sender message
 * @param chatId Identifier of receiver message if not null, else send message to all topic
 * @param message Current message
 */
data class ChatRequest(
    val senderId: String,
    val chatId: String?,
    val message: String,
)

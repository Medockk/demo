package com.foodback.demo.controller

import com.foodback.demo.dto.request.ChatRequest
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

/**
 * Controller to make HTTP-request
 */
//@Controller
//class ChatController(
//    private val simpleMessageTemplate: SimpMessagingTemplate
//) {
//
//    /**
//     * Method to send message
//     */
//    @MessageMapping("/chat")
//    fun sendMessage(message: ChatRequest) {
//        if (message.chatId != null) {
//            simpleMessageTemplate.convertAndSendToUser(
//                message.chatId, "/queue/messages", message
//            )
//        } else {
//            simpleMessageTemplate
//                .convertAndSend("/topic", message)
//        }
//    }
//}


class TextWebSocketHandler: TextWebSocketHandler() {

    private val sessions = mutableSetOf<WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        sessions.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        for (i in sessions) {
            if (i.isOpen) {
                i.sendMessage(TextMessage(message.payload))
            }
        }
    }
}
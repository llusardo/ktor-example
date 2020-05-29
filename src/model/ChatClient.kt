package com.example.model

import io.ktor.http.cio.websocket.DefaultWebSocketSession
import java.util.concurrent.atomic.AtomicInteger

class ChatClient(val session: DefaultWebSocketSession) {
    companion object { var lastId = AtomicInteger(0) }
    val id = lastId.getAndIncrement()
    val name = "user$id"
}
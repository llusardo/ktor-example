package com.example.websocket

import com.example.model.ChatClient
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Routing
import io.ktor.websocket.webSocket
import java.util.*

val clients = Collections.synchronizedSet(LinkedHashSet<ChatClient>())

fun Routing.chatWebSocketRoute() {
    webSocket("/chat") { // this: DefaultWebSocketSession
        val client = ChatClient(this)
        clients += client
        try {
            while (true) {
                val frame = incoming.receive()
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        // Iterate over all the connections
                        val textToSend = "${client.name} said: $text"
                        for (other in clients.toList()) {
                            other.session.outgoing.send(Frame.Text(textToSend))
                        }
                    }
                }
            }
        } finally {
            clients -= client
        }
    }
}
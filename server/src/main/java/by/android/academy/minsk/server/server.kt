package by.android.academy.minsk.server

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

// code was copied from https://ktor.io/quickstart/guides/chat.html

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        install(WebSockets)
        routing {
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }

            val clients = Collections.synchronizedSet(LinkedHashSet<ChatClient>())
            webSocket("/chat") {
                // this: DefaultWebSocketSession
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
            GlobalScope.launch {
                while (true) {
                    println("type broadcast message:")
                    val broadcastMessage = readLine()
                    for (other in clients.toList()) {
                        other.session.outgoing.send(Frame.Text(broadcastMessage ?: ""))
                    }
                }
            }
        }
    }
    server.start(wait = true)
}

class ChatClient(val session: DefaultWebSocketSession) {
    companion object {
        var lastId = AtomicInteger(0)
    }

    val id = lastId.getAndIncrement()
    val name = "user$id"
}
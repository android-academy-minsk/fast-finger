package by.android.academy.minsk.fastfinger.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

sealed class Frame {
    data class NewMessage(val text: String) : Frame()
    object ConnectionClosed : Frame()
    object Connecting : Frame()
    object Connected : Frame()
    object ConnectionError : Frame()
}

fun connectToChat(): Flow<Frame> = callbackFlow {
    val client = OkHttpClient.Builder()
        .readTimeout(500, TimeUnit.MILLISECONDS)
        .build()

    val request = Request.Builder()
        .url("ws://10.0.2.2:8080/chat")
        .build()

    val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("hello")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            offer(Frame.NewMessage(text))
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            offer(Frame.NewMessage(bytes.hex()))
        }
    }
    client.newWebSocket(request, webSocketListener)
    offer(Frame.Connecting)
}
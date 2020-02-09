package by.android.academy.minsk.fastfinger.chat

import by.android.academy.minsk.fastfinger.WEB_SOCKET_SERVER_URL
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

sealed class Frame {
    data class NewMessage(val text: String) : Frame()
    data class ConnectionClosed(val reason: String?) : Frame()
    object Connecting : Frame()
    object Connected : Frame()
    data class ReconnectingIn(val seconds: Int) : Frame()
    data class ConnectionError(val errorMessage: String) : Frame()
}

fun connectToChat(messagesToSend: ReceiveChannel<String>): Flow<Frame> = callbackFlow {
    val client = OkHttpClient.Builder()
        .readTimeout(500, TimeUnit.MILLISECONDS)
        .build()

    val request = Request.Builder()
        .url("$WEB_SOCKET_SERVER_URL/chat")
        .build()

    val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            //TODO(14): offer a frame
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (text.isNotEmpty()) {
                //TODO(14): offer a frame
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            val text = bytes.hex()
            if (text.isNotEmpty()) {
                //TODO(14): offer a frame
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            //TODO(14): offer a frame
            close()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            if (!isClosedForSend) {
                //TODO(14): offer a frame
                close()
            }
        }
    }
    val socket = client.newWebSocket(request, webSocketListener)
    //TODO(14): offer a frame
    //TODO(19): from channel to socket
    awaitClose {
        socket.close(1001, "by client initiative")
    }
}
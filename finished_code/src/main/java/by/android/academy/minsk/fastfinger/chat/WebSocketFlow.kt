package by.android.academy.minsk.fastfinger.chat

import by.android.academy.minsk.fastfinger.WEB_SOCKET_SERVER_URL
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

fun connectWithRetry(messagesToSend: ReceiveChannel<String>): Flow<Frame> =
    connectToChat(messagesToSend).flatMapLatest { frame ->
        when (frame) {
            is Frame.ConnectionError -> connectWithRetry(messagesToSend).onStart {
                emit(frame)
                emit(Frame.ReconnectingIn(3))
                delay(1000)
                emit(Frame.ReconnectingIn(2))
                delay(1000)
                emit(Frame.ReconnectingIn(1))
                delay(1000)
            }
            else -> flowOf(frame)
        }
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
            offer(Frame.Connected)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            //TODO(14): offer a frame
            offer(Frame.NewMessage(text))
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            val text = bytes.hex()
            //TODO(14): offer a frame
            offer(Frame.NewMessage(text))
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            //TODO(14): offer a frame
            offer(Frame.ConnectionError(t.message ?: "failed with unknown reason"))
            close()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            if (!isClosedForSend) {
                //TODO(14): offer a frame
                offer(Frame.ConnectionClosed(reason))
                close()
            }
        }
    }
    val socket = client.newWebSocket(request, webSocketListener)
    //TODO(14): offer a frame
    offer(Frame.Connecting)
    try {
        for (message in messagesToSend) {
            socket.send(message)
        }
    } finally {
        socket.close(1001, "by client initiative")
    }
}
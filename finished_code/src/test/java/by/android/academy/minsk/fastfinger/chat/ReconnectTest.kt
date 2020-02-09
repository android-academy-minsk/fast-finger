package by.android.academy.minsk.fastfinger.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals

typealias ReconnectFunc = Flow<Frame>.() -> Flow<Frame>

@RunWith(Parameterized::class)
class ReconnectTest(
    private val subject: ReconnectFunc
) {
    @Test
    fun errorConnecting() = runBlockingTest {
        var isFirstAttempt = true
        val source = flow {
            if (isFirstAttempt) {
                emit(Frame.Connecting)
                emit(Frame.ConnectionError("test"))
                isFirstAttempt = false
            } else {
                emit(Frame.Connecting)
                emit(Frame.Connected)
            }
        }

        val result = source.subject().toList(mutableListOf())

        assertEquals(
            listOf(
                Frame.Connecting,
                Frame.ConnectionError("test"),
                Frame.ReconnectingIn(3),
                Frame.ReconnectingIn(2),
                Frame.ReconnectingIn(1),
                Frame.Connecting,
                Frame.Connected
            ),
            result
        )
    }

    @Test
    fun reconnect() = runBlockingTest {
        var isFirstAttempt = true
        val source = flow {
            if (isFirstAttempt) {
                emit(Frame.Connecting)
                emit(Frame.Connected)
                emit(Frame.NewMessage("test message"))
                emit(Frame.ConnectionError("test error"))
                isFirstAttempt = false
            } else {
                emit(Frame.Connecting)
                emit(Frame.Connected)
            }
        }

        val result = source.subject().toList(mutableListOf())

        assertEquals(
            listOf(
                Frame.Connecting,
                Frame.Connected,
                Frame.NewMessage("test message"),
                Frame.ConnectionError("test error"),
                Frame.ReconnectingIn(3),
                Frame.ReconnectingIn(2),
                Frame.ReconnectingIn(1),
                Frame.Connecting,
                Frame.Connected
            ),
            result
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun provideImplementations(): Iterable<Array<Any>> = listOf(
            wrapSubject { reconnect() },
            wrapSubject { reconnectRx() }
        )
    }
}

private fun wrapSubject(subject: ReconnectFunc): Array<Any> = arrayOf(subject)

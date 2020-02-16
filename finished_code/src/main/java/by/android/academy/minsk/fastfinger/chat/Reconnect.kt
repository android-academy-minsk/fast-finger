package by.android.academy.minsk.fastfinger.chat

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

//!!!it's the FINISHED project, switch search to start module!!! TODO(16): reconnect imperative
fun Flow<Frame>.reconnect(): Flow<Frame> {
    val sourceFlow = this
    return flow {
        var retry = false
        do {
            sourceFlow.collect {
                retry = it is Frame.ConnectionError
                emit(it)
            }
            if (retry) {
                reconnectingInThreeSeconds(this)
            }
        } while (retry)
    }
}

//!!!it's the FINISHED project, switch search to start module!!! TODO(16): reconnect declarative
fun Flow<Frame>.reconnectRx(): Flow<Frame> = flatMapLatest { frame ->
    when (frame) {
        is Frame.ConnectionError -> reconnectRx().onStart {
            emit(frame)
            reconnectingInThreeSeconds(this)
        }
        else -> flowOf(frame)
    }
}

private suspend fun reconnectingInThreeSeconds(collector: FlowCollector<Frame>) {
    collector.emit(Frame.ReconnectingIn(3))
    delay(1000)
    collector.emit(Frame.ReconnectingIn(2))
    delay(1000)
    collector.emit(Frame.ReconnectingIn(1))
    delay(1000)
}
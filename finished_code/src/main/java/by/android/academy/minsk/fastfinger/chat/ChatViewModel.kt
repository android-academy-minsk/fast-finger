package by.android.academy.minsk.fastfinger.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val messageChannel = Channel<String>()

    private val _chatText = MutableLiveData<List<String>>(emptyList())
    val chatText: LiveData<List<String>> get() = _chatText

    init {
        //TODO: get text in the right way
        viewModelScope.launch {
            connectWithRetry(messageChannel).collect {
                when (it) {
                    is Frame.Connecting -> addText("connecting")
                    is Frame.NewMessage -> addText(it.text)
                    is Frame.ConnectionError -> addText(it.errorMessage)
                    is Frame.ReconnectingIn -> addText("reconnecting in ${it.seconds}")
                    is Frame.Connected -> addText("connected")
                    is Frame.ConnectionClosed -> addText(
                        "connection was closed: ${it.reason ?: "unknown reason"}"
                    )
                }
            }
        }
    }

    //TODO: what will be if call many times
    fun sendMessage(text: String) {
        viewModelScope.launch {
            messageChannel.send(text)
        }
    }

    private fun addText(text: String) {
        _chatText.value = _chatText.value!! + text
    }
}
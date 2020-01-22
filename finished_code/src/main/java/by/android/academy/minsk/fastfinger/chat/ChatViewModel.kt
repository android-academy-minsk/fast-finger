package by.android.academy.minsk.fastfinger.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _chatText = MutableLiveData<String>("")
    val chatText: LiveData<String> get() = _chatText

    init {
        viewModelScope.launch {
            connectWithRetry().collect {
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

    private fun addText(text: String) {
        _chatText.value = _chatText.value!! + "\n$text"
    }
}
package by.android.academy.minsk.fastfinger.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.android.academy.minsk.fastfinger.R
import by.android.academy.minsk.fastfinger.android.AndroidResourceManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(private val resource: AndroidResourceManager) : ViewModel() {

    private val messageChannel = Channel<String>()

    private val _chatText = MutableLiveData<List<String>>(emptyList())
    val chatText: LiveData<List<String>> get() = _chatText

    init {
        viewModelScope.launch {
            connectWithRetry(messageChannel).collect {
                showFrame(it)
            }
        }
    }

    private fun showFrame(frame: Frame) {
        val textToShow = when (frame) {
            is Frame.Connecting -> resource.getString(R.string.state_connecting)
            is Frame.NewMessage -> frame.text
            is Frame.ConnectionError -> frame.errorMessage
            is Frame.ReconnectingIn -> resource.getString(
                R.string.state_reconnecting,
                frame.seconds
            )
            is Frame.Connected -> resource.getString(R.string.state_connected)
            is Frame.ConnectionClosed ->
                resource.getString(
                    R.string.state_connection_closed, frame.reason
                        ?: resource.getString(R.string.state_unknown_reason)
                )
        }
        addText(textToShow)
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
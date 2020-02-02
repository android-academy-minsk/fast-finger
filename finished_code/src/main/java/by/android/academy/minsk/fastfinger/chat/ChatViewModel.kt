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
        //TODO: get text in the right way
        viewModelScope.launch {
            connectWithRetry(messageChannel).collect {
                when (it) {
                    is Frame.Connecting -> addText(resource.getString(R.string.state_connecting))
                    is Frame.NewMessage -> addText(it.text)
                    is Frame.ConnectionError -> addText(it.errorMessage)
                    is Frame.ReconnectingIn -> addText(resource.getString(R.string.state_reconnecting) + " " + "${it.seconds}")
                    is Frame.Connected -> addText(resource.getString(R.string.state_connected))
                    is Frame.ConnectionClosed -> addText(
                        resource.getString(R.string.state_connection_closed)
                                + (it.reason ?: resource.getString(R.string.state_unknown_reason))
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
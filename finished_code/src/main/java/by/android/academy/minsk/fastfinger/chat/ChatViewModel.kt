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
            connectToChat().collect {
                when (it) {
                    is Frame.Connecting -> addText("connecting")
                    is Frame.NewMessage -> addText(it.text)
                    is Frame.ConnectionError -> addText(it.errorMessage)
                }
            }
        }
    }

    private fun addText(text: String) {
        _chatText.value = _chatText.value!! + "\n$text"
    }
}
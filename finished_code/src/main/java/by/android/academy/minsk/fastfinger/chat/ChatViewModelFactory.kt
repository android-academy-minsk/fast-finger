package by.android.academy.minsk.fastfinger.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.android.academy.minsk.fastfinger.android.AndroidResourceManager

fun chatViewModelFactory(resourceManager: AndroidResourceManager) = object : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(resourceManager) as T
    }
}
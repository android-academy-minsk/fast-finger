package by.android.academy.minsk.fastfinger.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private var score = 0

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _button = MutableLiveData<ButtonState>(ButtonState.READY_TO_START)
    val button: LiveData<ButtonState> get() = _button

    fun onButtonClick() {
        when (button.value) {
            ButtonState.READY_TO_START -> onReadyToStartClick()
            ButtonState.GAME_IN_PROGRESS -> onInGameClick()
            ButtonState.STARTING, ButtonState.FINISHING -> {
            }
        }
    }

    private fun onReadyToStartClick() {
        viewModelScope.launch {
            launchGame()
        }
    }

    private fun onInGameClick() {
        score++
        _message.value = score.toString()
    }

    private suspend fun launchGame() {
        _button.value = ButtonState.STARTING
        _message.value = "READY"
        delay(500)
        _message.value = "STEADY"
        delay(500)
        startGame()
        delay(5000)
        finishGame()
    }

    private fun startGame() {
        score = 0
        _message.value = "GO!"
        _button.value = ButtonState.GAME_IN_PROGRESS
    }

    private suspend fun finishGame() {
        _message.value = "Your score is ${score}"
        _button.value = ButtonState.FINISHING
        delay(2000)
        _button.value = ButtonState.READY_TO_START
    }
}

enum class ButtonState {
    READY_TO_START,
    GAME_IN_PROGRESS,
    STARTING,
    FINISHING
}
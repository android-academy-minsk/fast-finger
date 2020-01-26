package by.android.academy.minsk.fastfinger.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.android.academy.minsk.fastfinger.ads.AdsApi
import by.android.academy.minsk.fastfinger.score.ScoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(
    private val scoreRepository: ScoreRepository,
    private val adsApi: AdsApi
) : ViewModel() {

    private var score = 0

    private val _advertisement = MutableLiveData<String>()
    val advertisement: LiveData<String> = _advertisement

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _button = MutableLiveData<ButtonState>(ButtonState.READY_TO_START)
    val button: LiveData<ButtonState> get() = _button

    private val _bestLocalScore = MutableLiveData("")
    val bestLocalScore: LiveData<String> get() = _bestLocalScore

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
        prepareGame()
        startGame()
        delay(5000)
        finishGame()
    }

    private suspend fun prepareGame() {
        _button.value = ButtonState.STARTING
        _message.value = "READY"
        delay(500)
        _message.value = "STEADY"
        delay(500)
    }

    private fun startGame() {
        score = 0
        _message.value = "GO!"
        _button.value = ButtonState.GAME_IN_PROGRESS
    }

    private suspend fun finishGame() {
        _message.value = "Your score is $score"
        val newBestLocalScore = scoreRepository.updateLocalBestScore(score)
        setBestLocalScore(newBestLocalScore)
        _button.value = ButtonState.FINISHING
        delay(2000)
        _button.value = ButtonState.READY_TO_START
    }

    fun onScreenOpen() {
        viewModelScope.launch {
            val bestScore = scoreRepository.getBestLocalScore()
            setBestLocalScore(bestScore)
            setupAdvertisement()
        }
    }

    private fun setBestLocalScore(bestScore: Int) {
        _bestLocalScore.value = if (bestScore > 0) {
            "Best score is $bestScore"
        } else {
            ""
        }
    }

    //TODO: handle errors in a good way
    private suspend fun setupAdvertisement() {
        _advertisement.value = try {
            adsApi.getAdvertisement()
        } catch (t: Throwable) {
            "error loading ad, enjoy!"
        }
    }
}

enum class ButtonState {
    READY_TO_START,
    GAME_IN_PROGRESS,
    STARTING,
    FINISHING
}
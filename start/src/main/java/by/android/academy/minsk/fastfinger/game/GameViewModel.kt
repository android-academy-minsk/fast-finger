package by.android.academy.minsk.fastfinger.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.android.academy.minsk.fastfinger.R
import by.android.academy.minsk.fastfinger.ads.AdsUseCase
import by.android.academy.minsk.fastfinger.ads.ShowAdsResult
import by.android.academy.minsk.fastfinger.android.AndroidResourceManager
import by.android.academy.minsk.fastfinger.score.BestScoreUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(
    private val bestScoreUseCase: BestScoreUseCase,
    private val adsUseCase: AdsUseCase,
    private val resource: AndroidResourceManager) : ViewModel() {

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
        // TODO(3): game play itself
    }

    private suspend fun launchGame() {
        prepareGame()
        startGame()
        delay(5000)
        finishGame()
    }

    private suspend fun prepareGame() {
        // TODO(1): ready and steady
    }

    private fun startGame() {
        // TODO(2): start the game
    }

    private suspend fun finishGame() {
        // TODO(8): pass score to logic
        // TODO(9): update ui with new best score (use setBestLocalScore function)
        // TODO(4): Finish the game
    }

    fun onScreenOpen() {
        viewModelScope.launch {
            // TODO(10): show best score when user open screen
        }
        viewModelScope.launch {
            setupAdvertisement()
        }
    }

    private fun setBestLocalScore(bestScore: Int) {
        _bestLocalScore.value = if (bestScore > 0) {
            resource.getString(R.string.game_your_best_score, bestScore)
        } else {
            ""
        }
    }

    private suspend fun setupAdvertisement() {
        //TODO(13): show advertisement on ui using _advertisement
    }
}

enum class ButtonState {
    READY_TO_START,
    GAME_IN_PROGRESS,
    STARTING,
    FINISHING
}
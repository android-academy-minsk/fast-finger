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
        // TODO(1): ready and steady
        _message.value = resource.getString(R.string.game_progress_type_ready)
        delay(500)
        _message.value = resource.getString(R.string.game_progress_type_steady)
        delay(500)
    }

    private fun startGame() {
        // TODO(2): start the game
        score = 0
        _message.value = resource.getString(R.string.game_progress_type_go)
        _button.value = ButtonState.GAME_IN_PROGRESS
    }

    private suspend fun finishGame() {
        // TODO(8): pass score to logic
        val newBestLocalScore = bestScoreUseCase.checkAndSaveBestScore(score)
        // TODO(9): update ui with new best score (use setBestLocalScore function)
        setBestLocalScore(newBestLocalScore)
        // TODO(4): Finish the game
        _message.value = resource.getString(R.string.game_your_score, score)
        _button.value = ButtonState.FINISHING
        delay(2000)
        _button.value = ButtonState.READY_TO_START
    }

    fun onScreenOpen() {
        viewModelScope.launch {
            // TODO(10): show best score when user open screen
            val bestScore = bestScoreUseCase.getBestLocalScore()
            setBestLocalScore(bestScore)
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
        _advertisement.value = when (val ads = adsUseCase.showAds()) {
            is ShowAdsResult.ShowLoadedAdvertisement -> ads.text
            ShowAdsResult.ShowAdsLoadingError -> resource.getString(R.string.error_loading_ads)
        }
    }
}

enum class ButtonState {
    READY_TO_START,
    GAME_IN_PROGRESS,
    STARTING,
    FINISHING
}
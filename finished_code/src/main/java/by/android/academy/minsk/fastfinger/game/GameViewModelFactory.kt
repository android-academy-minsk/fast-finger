package by.android.academy.minsk.fastfinger.game

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.android.academy.minsk.fastfinger.ads.createAdsUseCase
import by.android.academy.minsk.fastfinger.score.ScoreUseCase
import by.android.academy.minsk.fastfinger.score.getDatabase

fun gameViewModelFactory(context: Context) = object : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(ScoreUseCase(getDatabase(context).scoreDao), createAdsUseCase()) as T
    }
}
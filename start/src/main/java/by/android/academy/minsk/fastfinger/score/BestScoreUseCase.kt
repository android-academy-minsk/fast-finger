package by.android.academy.minsk.fastfinger.score

class BestScoreUseCase(private val dao: ScoreDao) {

    //TODO(7): read local best score
    suspend fun getBestLocalScore(): Int = 0

    //TODO(6): save best score using dao
    suspend fun checkAndSaveBestScore(score: Int): Int {
        return 0
    }
}
package by.android.academy.minsk.fastfinger.score

class BestScoreUseCase(private val dao: ScoreDao) {

    //TODO(6): get saved best score using dao
    suspend fun getBestLocalScore(): Int = 0

    //TODO(7): save best score using dao
    suspend fun checkAndSaveBestScore(score: Int): Int {
        return 0
    }
}
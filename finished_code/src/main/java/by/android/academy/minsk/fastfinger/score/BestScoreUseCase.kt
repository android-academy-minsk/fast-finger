package by.android.academy.minsk.fastfinger.score

class BestScoreUseCase(private val dao: ScoreDao) {

    //!!!it's the FINISHED project, switch search to start module!!! TODO(6): read local best score
    suspend fun getBestLocalScore(): Int = dao.getLocalBestScore() ?: 0

    //!!!it's the FINISHED project, switch search to start module!!! TODO(7): save best score using dao
    suspend fun checkAndSaveBestScore(score: Int): Int {
        val currentBestScore = getBestLocalScore()
        if (score > currentBestScore) {
            dao.updateLocalBestScore(score)
            return score
        }
        return currentBestScore
    }
}
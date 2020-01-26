package by.android.academy.minsk.fastfinger.score

class ScoreUseCase(private val dao: ScoreDao) {

    suspend fun getBestLocalScore(): Int = dao.getLocalBestScore() ?: 0

    suspend fun updateLocalBestScore(score: Int): Int {
        val currentBestScore = getBestLocalScore()
        if (score > currentBestScore) {
            dao.updateLocalBestScore(score)
            return score
        }
        return currentBestScore
    }
}
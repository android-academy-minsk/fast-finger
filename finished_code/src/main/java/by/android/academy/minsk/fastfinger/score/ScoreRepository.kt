package by.android.academy.minsk.fastfinger.score

class ScoreRepository(private val dao: ScoreDao) {

    suspend fun getBestLocalScore(): Int = dao.getScore(LOCAL_BEST_SCORE_ID)?.value ?: 0

    suspend fun updateLocalBestScore(score: Int): Int {
        val currentBestScore = getBestLocalScore()
        if (score > currentBestScore) {
            dao.updateScore(ScoreEntity(value = score, id = LOCAL_BEST_SCORE_ID))
            return score
        }
        return currentBestScore
    }
}
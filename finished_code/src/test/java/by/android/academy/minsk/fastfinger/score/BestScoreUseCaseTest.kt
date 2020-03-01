package by.android.academy.minsk.fastfinger.score

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import kotlin.test.Test
import kotlin.test.assertEquals

class BestScoreUseCaseTest {

    private val scoreDaoMock = mock<ScoreDao>()
    private val subject = BestScoreUseCase(scoreDaoMock)

    @After
    fun checkNoMoreInteractions() {
        verifyNoMoreInteractions(scoreDaoMock)
    }

    @Test
    fun `default best score should be 0`() = runBlocking<Unit> {
        whenever(scoreDaoMock.getLocalBestScore())
            .thenReturn(null)

        val result = subject.getBestLocalScore()

        assertEquals(0, result)
        verify(scoreDaoMock).getLocalBestScore()
    }

    @Test
    fun `save best score`() = runBlocking<Unit> {
        //arrange
        val scoreDoaMock: ScoreDao = mock()
        whenever(scoreDoaMock.getLocalBestScore())
            .thenReturn(13)
        val subject = BestScoreUseCase(scoreDoaMock)
        //act
        val result = subject.checkAndSaveBestScore(42)
        //assert
        assertEquals(42, result)
        verify(scoreDoaMock).getLocalBestScore()
        verify(scoreDoaMock).updateLocalBestScore(eq(42))
    }
}
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
    fun `should get best score from dao`() = runBlocking<Unit> {
        // arrange
        whenever(scoreDaoMock.getLocalBestScore())
            .thenReturn(33)
        // act
        val result = subject.getBestLocalScore()
        // assert
        assertEquals(33, result)
        verify(scoreDaoMock).getLocalBestScore()
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
        whenever(scoreDaoMock.getLocalBestScore())
            .thenReturn(13)
        //act
        val result = subject.checkAndSaveBestScore(42)
        //assert
        assertEquals(42, result)
        verify(scoreDaoMock).getLocalBestScore()
        verify(scoreDaoMock).updateLocalBestScore(eq(42))
    }

    @Test
    fun `shouldn't save not best score`() = runBlocking<Unit> {
        //arrange
        whenever(scoreDaoMock.getLocalBestScore())
            .thenReturn(33)
        //act
        val result = subject.checkAndSaveBestScore(25)
        //assert
        assertEquals(33, result)
        verify(scoreDaoMock).getLocalBestScore()
    }
}
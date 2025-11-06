package com.example.moodvibe.viewmodel

import com.example.moodvibe.data.Mood
import com.example.moodvibe.data.MoodHistoryEntity
import com.example.moodvibe.data.MoodRepository
import com.example.moodvibe.domain.GetMoodsUseCase
import com.example.moodvibe.domain.GetQuoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MoodViewModelTest {

    private lateinit var viewModel: MoodViewModel
    private val repository: MoodRepository = mock()
    private lateinit var getMoodsUseCase: GetMoodsUseCase
    private lateinit var getQuoteUseCase: GetQuoteUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockMoods = listOf(
        Mood(
            name = "Радость",
            imageRes = 1,
            quotes = listOf("Счастье — это выбор!", "Сияй как солнце!")
        ),
        Mood(
            name = "Спокойствие",
            imageRes = 2,
            quotes = listOf("Покой приходит изнутри")
        )
    )

    private val mockHistory = listOf(
        MoodHistoryEntity(
            id = 1,
            moodName = "Радость",
            quote = "Счастье — это выбор!",
            timestamp = System.currentTimeMillis()
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getMoodsUseCase = GetMoodsUseCase(repository)
        getQuoteUseCase = GetQuoteUseCase(repository)

        runTest {
            whenever(repository.getMoods()).thenReturn(mockMoods)
            whenever(repository.getMoodHistory()).thenReturn(flowOf(mockHistory))
        }

        viewModel = MoodViewModel(getMoodsUseCase, getQuoteUseCase, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test moods are loaded correctly on init`() = runTest {
        // завершение корутины
        advanceUntilIdle()

        val moods = viewModel.moods.value
        assertEquals(2, moods.size, "Должно быть загружено 2 настроения")
        assertEquals("Радость", moods[0].name, "Первое настроение должно быть 'Радость'")
        assertEquals("Спокойствие", moods[1].name, "Второе настроение должно быть 'Спокойствие'")
    }

    @Test
    fun `test history is loaded correctly on init`() = runTest {
        advanceUntilIdle()

        // проверка загрузки истории
        val history = viewModel.moodHistory.value
        assertEquals(1, history.size, "Должна быть 1 запись в истории")
        assertEquals("Радость", history[0].moodName, "Настроение в истории должно быть 'Радость'")
        assertEquals("Счастье — это выбор!", history[0].quote, "Цитата должна совпадать")
    }

    @Test
    fun `test loadQuote returns correct quote for mood`() = runTest {
        whenever(repository.getQuoteForMood("Радость"))
            .thenReturn("Счастье — это выбор!")

        viewModel.loadQuote("Радость")
        advanceUntilIdle()

        assertEquals("Счастье — это выбор!", viewModel.quote.value, "Цитата должна совпадать")
        verify(repository).saveMoodToHistory("Радость", "Счастье — это выбор!")
    }

    @Test
    fun `test loadQuote returns default for unknown mood`() = runTest {
        whenever(repository.getQuoteForMood("Unknown"))
            .thenReturn("Оставайся вдохновлённым!")

        viewModel.loadQuote("Unknown")
        advanceUntilIdle()

        assertEquals("Оставайся вдохновлённым!", viewModel.quote.value)
        verify(repository).saveMoodToHistory("Unknown", "Оставайся вдохновлённым!")
    }

    @Test
    fun `test deleteHistoryItem calls repository`() = runTest {
        val itemId = 1L

        viewModel.deleteHistoryItem(itemId)
        advanceUntilIdle()

        verify(repository).deleteMoodHistory(itemId)
    }

    @Test
    fun `test clearAllHistory calls repository`() = runTest {
        viewModel.clearAllHistory()
        advanceUntilIdle()

        verify(repository).clearHistory()
    }

    @Test
    fun `test multiple quote loads update state correctly`() = runTest {
        whenever(repository.getQuoteForMood("Радость"))
            .thenReturn("Счастье — это выбор!")

        viewModel.loadQuote("Радость")
        advanceUntilIdle()
        assertEquals("Счастье — это выбор!", viewModel.quote.value)

        whenever(repository.getQuoteForMood("Спокойствие"))
            .thenReturn("Покой приходит изнутри")

        viewModel.loadQuote("Спокойствие")
        advanceUntilIdle()
        assertEquals("Покой приходит изнутри", viewModel.quote.value)
    }
}
package com.example.moodvibe.viewmodel

import com.example.moodvibe.data.Mood
import com.example.moodvibe.data.MoodRepository
import com.example.moodvibe.domain.GetMoodsUseCase
import com.example.moodvibe.domain.GetQuoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MoodViewModelTest {

    private lateinit var viewModel: MoodViewModel
    private val repository: MoodRepository = mock()
    private lateinit var getMoodsUseCase: GetMoodsUseCase
    private lateinit var getQuoteUseCase: GetQuoteUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getMoodsUseCase = GetMoodsUseCase(repository)
        getQuoteUseCase = GetQuoteUseCase(repository)

        val mockMoods = listOf(
            Mood("Joy", listOf("#FF6F61", "#FFD700"), listOf("Happiness is a choice!"))
        )

        runTest {
            whenever(repository.getMoods()).thenReturn(mockMoods)
        }

        viewModel = MoodViewModel(getMoodsUseCase, getQuoteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test moods are loaded correctly`() = runTest {
        val mockMoods = listOf(
            Mood("Joy", listOf("#FF6F61", "#FFD700"), listOf("Happiness is a choice!"))
        )
        whenever(repository.getMoods()).thenReturn(mockMoods)

        advanceUntilIdle()

        val moods = viewModel.moods.value
        assertEquals(1, moods.size)
        assertEquals("Joy", moods[0].name)
    }

    @Test
    fun `test quote is loaded for mood`() = runTest {
        whenever(repository.getQuoteForMood("Joy")).thenReturn("Happiness is a choice!")

        viewModel.loadQuote("Joy")
        advanceUntilIdle()

        assertEquals("Happiness is a choice!", viewModel.quote.value)
    }

    @Test
    fun `test quote returns default for unknown mood`() = runTest {
        whenever(repository.getQuoteForMood("Unknown")).thenReturn("Stay inspired!")

        viewModel.loadQuote("Unknown")
        advanceUntilIdle()

        assertEquals("Stay inspired!", viewModel.quote.value)
    }
}
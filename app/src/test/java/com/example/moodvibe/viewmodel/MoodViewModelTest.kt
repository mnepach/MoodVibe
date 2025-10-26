package com.example.moodvibe.viewmodel

import com.example.moodvibe.data.Mood
import com.example.moodvibe.data.MoodRepository
import com.example.moodvibe.domain.GetMoodsUseCase
import com.example.moodvibe.domain.GetQuoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MoodViewModelTest {

    private lateinit var viewModel: MoodViewModel
    private val repository: MoodRepository = mock()
    private val getMoodsUseCase = GetMoodsUseCase(repository)
    private val getQuoteUseCase = GetQuoteUseCase(repository)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MoodViewModel(getMoodsUseCase, getQuoteUseCase)
    }

    @Test
    fun `test moods are loaded correctly`() = runTest {
        val mockMoods = listOf(
            Mood("Joy", listOf("#FF6F61", "#FFD700"), listOf("Happiness is a choice!"))
        )
        whenever(repository.getMoods()).thenReturn(mockMoods)

        val moods = viewModel.moods.value
        assertEquals(mockMoods, moods)
    }

    @Test
    fun `test quote is loaded for mood`() = runTest {
        whenever(repository.getQuoteForMood("Joy")).thenReturn("Happiness is a choice!")
        viewModel.loadQuote("Joy")
        assertEquals("Happiness is a choice!", viewModel.quote.value)
    }
}
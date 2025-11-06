package com.example.moodvibe.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class MoodRepositoryTest {

    private lateinit var repository: MoodRepository
    private val mockDao: MoodHistoryDao = mock()

    @Before
    fun setup() {
        repository = MoodRepository(mockDao)
    }

    @Test
    fun `getMoods returns all moods`() = runTest {
        val moods = repository.getMoods()

        assertEquals(7, moods.size, "Должно быть 7 настроений")
        assertTrue(moods.any { it.name == "Радость" }, "Должно быть настроение 'Радость'")
        assertTrue(moods.any { it.name == "Спокойствие" }, "Должно быть настроение 'Спокойствие'")
    }

    @Test
    fun `getQuoteForMood returns random quote for existing mood`() = runTest {
        val quote = repository.getQuoteForMood("Радость")

        assertNotNull(quote, "Цитата не должна быть null")
        assertTrue(quote.isNotEmpty(), "Цитата не должна быть пустой")

        // Проверяем что цитата из списка для "Радость"
        val joyMood = moods.find { it.name == "Радость" }
        assertTrue(
            joyMood?.quotes?.contains(quote) == true,
            "Цитата должна быть из списка настроения 'Радость'"
        )
    }

    @Test
    fun `getQuoteForMood returns default for non-existing mood`() = runTest {
        val quote = repository.getQuoteForMood("НесуществующееНастроение")

        assertEquals("Оставайся вдохновлённым!", quote, "Должна вернуться дефолтная цитата")
    }

    @Test
    fun `saveMoodToHistory saves entity to dao`() = runTest {
        val moodName = "Радость"
        val quote = "Счастье — это выбор!"

        repository.saveMoodToHistory(moodName, quote)

        verify(mockDao).insert(
            org.mockito.kotlin.argThat { entity ->
                entity.moodName == moodName && entity.quote == quote
            }
        )
    }

    @Test
    fun `getMoodHistory returns flow from dao`() = runTest {
        val mockHistory = listOf(
            MoodHistoryEntity(1, "Радость", "Счастье — это выбор!", System.currentTimeMillis())
        )
        whenever(mockDao.getAllHistory()).thenReturn(flowOf(mockHistory))

        val history = repository.getMoodHistory().first()

        assertEquals(1, history.size, "Должна быть 1 запись")
        assertEquals("Радость", history[0].moodName, "Настроение должно быть 'Радость'")
    }

    @Test
    fun `deleteMoodHistory calls dao delete`() = runTest {
        val itemId = 1L

        repository.deleteMoodHistory(itemId)

        verify(mockDao).delete(itemId)
    }

    @Test
    fun `clearHistory calls dao deleteAll`() = runTest {
        repository.clearHistory()

        verify(mockDao).deleteAll()
    }
}
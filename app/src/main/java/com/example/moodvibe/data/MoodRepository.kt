package com.example.moodvibe.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoodRepository @Inject constructor(
    private val moodHistoryDao: MoodHistoryDao
) {
    suspend fun getMoods(): List<Mood> = withContext(Dispatchers.Default) {
        moods
    }

    suspend fun getQuoteForMood(moodName: String): String = withContext(Dispatchers.Default) {
        moods.find { it.name == moodName }?.quotes?.random() ?: "Оставайся вдохновлённым!"
    }

    // история настроений
    suspend fun saveMoodToHistory(moodName: String, quote: String) {
        val entity = MoodHistoryEntity(
            moodName = moodName,
            quote = quote
        )
        moodHistoryDao.insert(entity)
    }

    fun getMoodHistory(): Flow<List<MoodHistoryEntity>> {
        return moodHistoryDao.getAllHistory()
    }

    fun getRecentHistory(): Flow<List<MoodHistoryEntity>> {
        return moodHistoryDao.getRecentHistory()
    }

    suspend fun deleteMoodHistory(id: Long) {
        moodHistoryDao.delete(id)
    }

    suspend fun clearHistory() {
        moodHistoryDao.deleteAll()
    }
}
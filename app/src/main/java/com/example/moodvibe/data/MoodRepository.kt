package com.example.moodvibe.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoodRepository {
    suspend fun getMoods(): List<Mood> = withContext(Dispatchers.Default) {
        moods
    }

    suspend fun getQuoteForMood(moodName: String): String = withContext(Dispatchers.Default) {
        moods.find { it.name == moodName }?.quotes?.random() ?: "Stay inspired!"
    }
}

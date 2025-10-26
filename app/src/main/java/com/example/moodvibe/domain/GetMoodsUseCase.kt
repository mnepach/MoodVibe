package com.example.moodvibe.domain

import com.example.moodvibe.data.Mood
import com.example.moodvibe.data.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMoodsUseCase @Inject constructor(private val repository: MoodRepository) {
    operator fun invoke(): Flow<List<Mood>> = flow {
        emit(repository.getMoods())
    }
}

class GetQuoteUseCase @Inject constructor(private val repository: MoodRepository) {
    suspend operator fun invoke(moodName: String): String {
        return repository.getQuoteForMood(moodName)
    }
}
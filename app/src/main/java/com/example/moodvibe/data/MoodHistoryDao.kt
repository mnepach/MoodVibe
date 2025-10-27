package com.example.moodvibe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodHistoryDao {
    @Insert
    suspend fun insert(moodHistory: MoodHistoryEntity)

    @Query("SELECT * FROM mood_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<MoodHistoryEntity>>

    @Query("SELECT * FROM mood_history ORDER BY timestamp DESC LIMIT 10")
    fun getRecentHistory(): Flow<List<MoodHistoryEntity>>

    @Query("DELETE FROM mood_history WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM mood_history")
    suspend fun deleteAll()
}
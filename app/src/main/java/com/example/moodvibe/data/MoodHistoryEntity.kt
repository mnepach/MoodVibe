package com.example.moodvibe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_history")
data class MoodHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val moodName: String,
    val quote: String,
    val timestamp: Long = System.currentTimeMillis()
)
package com.example.moodvibe.di

import android.content.Context
import androidx.room.Room
import com.example.moodvibe.data.AppDatabase
import com.example.moodvibe.data.MoodHistoryDao
import com.example.moodvibe.data.MoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "moodvibe_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMoodHistoryDao(database: AppDatabase): MoodHistoryDao {
        return database.moodHistoryDao()
    }

    @Provides
    @Singleton
    fun provideMoodRepository(moodHistoryDao: MoodHistoryDao): MoodRepository {
        return MoodRepository(moodHistoryDao)
    }
}
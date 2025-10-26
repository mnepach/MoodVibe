package com.example.moodvibe.di

import com.example.moodvibe.data.MoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMoodRepository(): MoodRepository {
        return MoodRepository()
    }
}
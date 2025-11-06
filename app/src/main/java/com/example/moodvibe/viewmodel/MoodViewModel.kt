package com.example.moodvibe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodvibe.data.Mood
import com.example.moodvibe.data.MoodHistoryEntity
import com.example.moodvibe.domain.GetMoodsUseCase
import com.example.moodvibe.domain.GetQuoteUseCase
import com.example.moodvibe.data.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val getMoodsUseCase: GetMoodsUseCase,
    private val getQuoteUseCase: GetQuoteUseCase,
    private val repository: MoodRepository
) : ViewModel() {

    private val _moods = MutableStateFlow<List<Mood>>(emptyList())
    val moods: StateFlow<List<Mood>> = _moods.asStateFlow()

    private val _quote = MutableStateFlow("")
    val quote: StateFlow<String> = _quote.asStateFlow()

    private val _moodHistory = MutableStateFlow<List<MoodHistoryEntity>>(emptyList())
    val moodHistory: StateFlow<List<MoodHistoryEntity>> = _moodHistory.asStateFlow()

    init {
        loadMoods()
        loadHistory()
    }

    private fun loadMoods() {
        viewModelScope.launch {
            getMoodsUseCase().collect { moods ->
                _moods.value = moods
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getMoodHistory().collect { history ->
                _moodHistory.value = history
            }
        }
    }

    fun loadQuote(moodName: String) {
        viewModelScope.launch {
            val newQuote = getQuoteUseCase(moodName)
            _quote.value = newQuote

            // сохранение в историю
            repository.saveMoodToHistory(moodName, newQuote)
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteMoodHistory(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
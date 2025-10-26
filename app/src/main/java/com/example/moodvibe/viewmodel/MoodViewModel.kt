package com.example.moodvibe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodvibe.data.Mood
import com.example.moodvibe.domain.GetMoodsUseCase
import com.example.moodvibe.domain.GetQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val getMoodsUseCase: GetMoodsUseCase,
    private val getQuoteUseCase: GetQuoteUseCase
) : ViewModel() {

    private val _moods = MutableStateFlow<List<Mood>>(emptyList())
    val moods: StateFlow<List<Mood>> = _moods

    private val _quote = MutableStateFlow("")
    val quote: StateFlow<String> = _quote

    init {
        viewModelScope.launch {
            getMoodsUseCase().collect { moods ->
                _moods.value = moods
            }
        }
    }

    fun loadQuote(moodName: String) {
        viewModelScope.launch {
            _quote.value = getQuoteUseCase(moodName)
        }
    }
}
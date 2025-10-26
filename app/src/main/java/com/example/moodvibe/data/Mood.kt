package com.example.moodvibe.data

data class Mood(
    val name: String,
    val gradientColors: List<String>,
    val quotes: List<String>
)

val moods = listOf(
    Mood(
        name = "Joy",
        gradientColors = listOf("#FF6F61", "#FFD700"),
        quotes = listOf("Happiness is a choice!", "Shine like the sun!")
    ),
    Mood(
        name = "Calm",
        gradientColors = listOf("#4682B4", "#87CEEB"),
        quotes = listOf("Peace comes from within.", "Breathe deeply.")
    ),
    Mood(
        name = "Sad",
        gradientColors = listOf("#4682B4", "#2F4F4F"),
        quotes = listOf("It's okay to feel.", "Tomorrow is a new day.")
    )
)
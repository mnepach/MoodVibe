package com.example.moodvibe.data

data class Mood(
    val name: String,
    val gradientColors: List<String>, // Используем первый элемент для эмодзи
    val quotes: List<String>
)

val moods = listOf(
    Mood(
        name = "Joy",
        gradientColors = listOf("😊", "✨"), // Эмодзи вместо цветов
        quotes = listOf(
            "Happiness is a choice!",
            "Shine like the sun!",
            "Today is a beautiful day to be alive",
            "Your smile is contagious"
        )
    ),
    Mood(
        name = "Calm",
        gradientColors = listOf("😌", "🧘"),
        quotes = listOf(
            "Peace comes from within",
            "Breathe deeply",
            "Find your inner zen",
            "Stillness speaks volumes"
        )
    ),
    Mood(
        name = "Excited",
        gradientColors = listOf("🤩", "🎉"),
        quotes = listOf(
            "Life is an adventure!",
            "Chase your dreams",
            "Every moment is a gift",
            "The best is yet to come"
        )
    ),
    Mood(
        name = "Thoughtful",
        gradientColors = listOf("🤔", "💭"),
        quotes = listOf(
            "Deep thoughts lead to wisdom",
            "Reflection brings clarity",
            "Think before you act",
            "Knowledge is power"
        )
    ),
    Mood(
        name = "Loved",
        gradientColors = listOf("🥰", "💖"),
        quotes = listOf(
            "Love is all around",
            "You are deeply appreciated",
            "Spread love everywhere",
            "Love yourself first"
        )
    ),
    Mood(
        name = "Sad",
        gradientColors = listOf("😢", "🌧️"),
        quotes = listOf(
            "It's okay to feel",
            "Tomorrow is a new day",
            "This too shall pass",
            "Be gentle with yourself"
        )
    ),
    Mood(
        name = "Energetic",
        gradientColors = listOf("⚡", "🔥"),
        quotes = listOf(
            "Unleash your power!",
            "Nothing can stop you",
            "Energy flows where attention goes",
            "You've got this!"
        )
    )
)
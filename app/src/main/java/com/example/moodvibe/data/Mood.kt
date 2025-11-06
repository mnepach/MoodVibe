package com.example.moodvibe.data

import com.example.moodvibe.R

data class Mood(
    val name: String,
    val imageRes: Int, // айди изображения
    val quotes: List<String>
)

val moods = listOf(
    Mood(
        name = "Радость",
        imageRes = R.drawable.mood_joy,
        quotes = listOf(
            "Счастье — это выбор!",
            "Сияй как солнце!",
            "Сегодня прекрасный день",
            "Твоя улыбка заразительна"
        )
    ),
    Mood(
        name = "Спокойствие",
        imageRes = R.drawable.mood_calm,
        quotes = listOf(
            "Покой приходит изнутри",
            "Алкоголь убивает нервные клетки - остаются только спокойные",
            "Найди свой внутренний дзен",
            "Тишина говорит о многом"
        )
    ),
    Mood(
        name = "Восторг",
        imageRes = R.drawable.mood_excited,
        quotes = listOf(
            "Моя жизнь в двух состояниях: светлая и нефильтрованная",
            "Следуй за мечтой",
            "Каждый момент — это подарок",
            "Лучшее ещё впереди"
        )
    ),
    Mood(
        name = "Раздумье",
        imageRes = R.drawable.mood_thoughtful,
        quotes = listOf(
            "Глубокие мысли ведут к мудрости",
            "Размышление приносит ясность",
            "Подумай, прежде чем действовать",
            "Знание — сила"
        )
    ),
    Mood(
        name = "Любовь",
        imageRes = R.drawable.mood_loved,
        quotes = listOf(
            "Любовь повсюду",
            "Тебя глубоко ценят",
            "Распространяй любовь везде",
            "Люби себя в первую очередь"
        )
    ),
    Mood(
        name = "Грусть",
        imageRes = R.drawable.mood_sad,
        quotes = listOf(
            "Это нормально — чувствовать",
            "Завтра новый день",
            "И это пройдёт",
            "Будь добр к себе"
        )
    ),
    Mood(
        name = "Энергия",
        imageRes = R.drawable.mood_energetic,
        quotes = listOf(
            "Раскрой свою силу!",
            "Ничто не остановит тебя",
            "Энергия течёт туда, куда направлено внимание",
            "У тебя всё получится!"
        )
    )
)
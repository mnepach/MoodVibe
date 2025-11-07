package com.example.moodvibe.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainScreenUITest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.waitForIdle()
        Thread.sleep(1500) // небольшая пауза на анимации и загрузку
    }

    @Test
    fun testMainScreenDisplaysTitle() {
        try {
            composeTestRule.waitForIdle()
            composeTestRule
                .onNodeWithText("✨ Как твоё настроение?", useUnmergedTree = true)
                .assertExists("Заголовок не найден")
                .assertIsDisplayed()
        } catch (e: Exception) {
            composeTestRule.onRoot().printToLog("UI_TREE_TITLE")
            throw e
        }
    }

    @Test
    fun testMainScreenDisplaysSubtitle() {
        try {
            composeTestRule.waitForIdle()
            composeTestRule
                .onNodeWithText("Выбери своё состояние", useUnmergedTree = true)
                .assertExists("Подзаголовок не найден")
                .assertIsDisplayed()
        } catch (e: Exception) {
            composeTestRule.onRoot().printToLog("UI_TREE_SUBTITLE")
            throw e
        }
    }

    @Test
    fun testMoodCardsAreDisplayed() {
        try {
            composeTestRule.waitUntil(timeoutMillis = 10000) {
                composeTestRule.onAllNodesWithText("Радость", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule
                .onNodeWithText("Радость", useUnmergedTree = true)
                .assertExists("Карточка 'Радость' не найдена")
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText("Спокойствие", useUnmergedTree = true)
                .assertExists("Карточка 'Спокойствие' не найдена")
                .assertIsDisplayed()
        } catch (e: Exception) {
            composeTestRule.onRoot().printToLog("UI_TREE_CARDS")
            throw e
        }
    }

    @Test
    fun testMoodCardClickNavigatesToDetail() {
        try {
            composeTestRule.waitUntil(timeoutMillis = 10000) {
                composeTestRule.onAllNodesWithText("Радость", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule
                .onNodeWithText("Радость", useUnmergedTree = true)
                .performClick()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithText("🔄 Получить другую цитату", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule
                .onNodeWithText("🔄 Получить другую цитату", useUnmergedTree = true)
                .assertExists("Кнопка получения цитаты не найдена")
                .assertIsDisplayed()
        } catch (e: Exception) {
            composeTestRule.onRoot().printToLog("UI_TREE_DETAIL")
            throw e
        }
    }

    @Test
    fun testBackNavigationFromDetailScreen() {
        try {
            composeTestRule.waitUntil(timeoutMillis = 10000) {
                composeTestRule.onAllNodesWithText("Радость", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithText("Радость", useUnmergedTree = true)
                .performClick()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithText("✕", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithText("✕", useUnmergedTree = true)
                .performClick()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithText("✨ Как твоё настроение?", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithText("✨ Как твоё настроение?", useUnmergedTree = true)
                .assertExists("Не вернулись на главный экран")
                .assertIsDisplayed()
        } catch (e: Exception) {
            composeTestRule.onRoot().printToLog("UI_TREE_BACK")
            throw e
        }
    }

    @Test
    fun testHistoryButtonAppearsWhenHistoryExists() {
        try {
            // 1️⃣ Создаём запись — кликаем на настроение
            composeTestRule.waitUntil(timeoutMillis = 10000) {
                composeTestRule.onAllNodesWithText("Радость", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithText("Радость", useUnmergedTree = true)
                .performClick()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithText("✕", useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            // 2️⃣ Возвращаемся
            composeTestRule.onNodeWithText("✕", useUnmergedTree = true)
                .performClick()

            // 3️⃣ Ждём обновления главного экрана
            composeTestRule.waitUntil(timeoutMillis = 8000) {
                composeTestRule.onAllNodesWithText("История", substring = true, useUnmergedTree = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            // 4️⃣ Проверяем наличие кнопки, даже если она вне экрана
            composeTestRule.onNodeWithText("История", substring = true, useUnmergedTree = true)
                .performScrollTo()
                .assertExists("Кнопка истории не появилась")
                .assertIsDisplayed()

        } catch (e: Exception) {
            composeTestRule.onRoot().printToLog("UI_TREE_HISTORY")
            throw e
        }
    }
}

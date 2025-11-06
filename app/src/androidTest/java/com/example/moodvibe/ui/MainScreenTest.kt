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
    }

    @Test
    fun testMainScreenDisplaysTitle() {
        composeTestRule
            .onNodeWithText("✨ Как твоё настроение?")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testMainScreenDisplaysSubtitle() {
        composeTestRule
            .onNodeWithText("Выбери своё состояние")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testMoodCardsAreDisplayed() {
        composeTestRule
            .onNodeWithText("Радость")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Спокойствие")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testMoodCardClickNavigatesToDetail() {
        composeTestRule
            .onNodeWithText("Радость")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Радость")
            .assertExists()

        composeTestRule
            .onNodeWithText("🔄 Получить другую цитату")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testBackNavigationFromDetailScreen() {
        composeTestRule
            .onNodeWithText("Радость")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("✕")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("✨ Как твоё настроение?")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testHistoryButtonAppearsWhenHistoryExists() {
        composeTestRule
            .onNodeWithText("Радость")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("✕")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("История настроений")
            .assertExists()
            .assertIsDisplayed()
    }
}
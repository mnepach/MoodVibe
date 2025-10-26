package com.example.moodvibe.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testMoodCardClickNavigatesToDetail() {
        composeTestRule.onNodeWithText("Joy").performClick()
        composeTestRule.onNodeWithText("Happiness is a choice!").assertExists()
    }
}
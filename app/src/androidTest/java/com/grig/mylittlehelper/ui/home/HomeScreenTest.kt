package com.grig.mylittlehelper.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.grig.core.theme.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysChooseYourPathHeader() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                HomeScreen(navigateToMal = {}, navigateToDanish = {})
            }
        }

        composeTestRule.onNodeWithText("Choose Your Path.").assertIsDisplayed()
    }

    @Test
    fun displaysMalCard() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                HomeScreen(navigateToMal = {}, navigateToDanish = {})
            }
        }

        composeTestRule.onNodeWithText("MyAnimeList").assertIsDisplayed()
    }

    @Test
    fun displaysDanishCard() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                HomeScreen(navigateToMal = {}, navigateToDanish = {})
            }
        }

        composeTestRule.onNodeWithText("Learn Danish").assertIsDisplayed()
    }

    @Test
    fun malCardClickTriggersCallback() {
        var malClicked = false
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                HomeScreen(navigateToMal = { malClicked = true }, navigateToDanish = {})
            }
        }

        composeTestRule.onNodeWithText("MyAnimeList").performClick()
        assertTrue(malClicked)
    }

    @Test
    fun danishCardClickTriggersCallback() {
        var danishClicked = false
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                HomeScreen(navigateToMal = {}, navigateToDanish = { danishClicked = true })
            }
        }

        composeTestRule.onNodeWithText("Learn Danish").performClick()
        assertTrue(danishClicked)
    }
}

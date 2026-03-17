package com.grig.myanimelist.ui.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.ui.MalEmpty
import com.grig.myanimelist.ui.MalError
import com.grig.myanimelist.ui.MalLoading
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MalHomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            MaterialTheme {
                MalLoading()
            }
        }

        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun emptyState_rendersCorrectly() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                MalEmpty(activeTab = MalTab.Anime)
            }
        }

        composeTestRule.onNodeWithText("No anime Yet").assertIsDisplayed()
    }

    @Test
    fun errorState_showsRetryButton() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                MalError(
                    activeTab = MalTab.Anime,
                    exception = RuntimeException("Test error"),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()
        composeTestRule.onNodeWithText("Something Went Wrong").assertIsDisplayed()
    }

    @Test
    fun tabToggle_displaysAnimeAndMangaTabs() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                TabToggle(
                    activeTab = MalTab.Anime,
                    onTabSelected = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Anime").assertIsDisplayed()
        composeTestRule.onNodeWithText("Manga").assertIsDisplayed()
    }

    @Test
    fun tabToggle_clickMangaTriggersCallback() {
        var selectedTab: MalTab? = null
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                TabToggle(
                    activeTab = MalTab.Anime,
                    onTabSelected = { selectedTab = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Manga").performClick()
        assertEquals(MalTab.Manga, selectedTab)
    }
}

package com.example.favorites

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun favoritesScreen_showsEmptyState() {
        composeTestRule.setContent {
            FavoritesScreen(navHostController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Нет избранных").assertExists()
    }
}

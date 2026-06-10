package com.example.search

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchScreen_showsSearchBar() {
        composeTestRule.setContent {
            SearchBarVidjet(
                navHostController = rememberNavController(),
                searchQuery = "",
                onSearchQueryChange = {}
            )
        }

        composeTestRule.onNodeWithText("Город, экскурсия, билет, доставка").assertExists()
    }
}

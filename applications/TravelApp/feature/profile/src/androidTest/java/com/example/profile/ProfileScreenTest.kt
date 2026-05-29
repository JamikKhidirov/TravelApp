package com.example.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_showsProfileTitle() {
        composeTestRule.setContent {
            ProfileScreen(navHostController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Профиль").assertExists()
    }
}

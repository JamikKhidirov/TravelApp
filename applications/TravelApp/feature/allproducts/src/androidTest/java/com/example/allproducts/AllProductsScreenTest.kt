package com.example.allproducts

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class AllProductsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun allProductsScreen_showsTitle() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AllProductsScreen(navHostController = navController)
        }

        composeTestRule.onNodeWithText("Все туры").assertExists()
    }
}

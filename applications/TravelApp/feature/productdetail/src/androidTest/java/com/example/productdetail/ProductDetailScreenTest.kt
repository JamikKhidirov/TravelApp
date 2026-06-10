package com.example.productdetail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class ProductDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailScreen_showsDetailLabel() {
        composeTestRule.setContent {
            ProductDetailScreen(navHostController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Детали тура").assertExists()
    }
}

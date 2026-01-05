package com.example.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.domain.data.citydata.CityDto
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingState() {
        composeTestRule.setContent {
            BaseScreenContainer(
                isLoading = true,
                isRefreshing = false,
                errorMessage = null,
                onRefresh = {}
            ) {
                // Пустой контент
            }
        }

        // Проверяем, что отображается индикатор загрузки
        composeTestRule.onNodeWithTag(TEST_TAG_LOADING_WHEEL).assertIsDisplayed()
    }

    @Test
    fun testErrorState() {
        val errorMsg = "Ошибка сети"
        
        composeTestRule.setContent {
            BaseScreenContainer(
                isLoading = false,
                isRefreshing = false,
                errorMessage = errorMsg,
                onRefresh = {}
            ) {
                // Пустой контент
            }
        }

        // Проверяем, что текст ошибки отображается
        composeTestRule.onNodeWithTag(TEST_TAG_ERROR_TEXT).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMsg).assertIsDisplayed()
    }

    @Test
    fun testEmptyState() {
        composeTestRule.setContent {
            BottomMainScreenContent(
                cities = emptyList(),
                onCLickCity = {}
            )
        }

        // Проверяем, что отображается текст "Города не найдены"
        composeTestRule.onNodeWithTag(TEST_TAG_EMPTY_TEXT).assertIsDisplayed()
        composeTestRule.onNodeWithText("Города не найдены").assertIsDisplayed()
    }

    @Test
    fun testContentState() {
        val fakeCities = listOf(
            CityDto(
                id = 1,
                name = "Москва",
                region_id = 1,
                country_id = 1
            ),
            CityDto(
                id = 2,
                name = "Санкт-Петербург",
                region_id = 2,
                country_id = 1
            )
        )

        composeTestRule.setContent {
            BottomMainScreenContent(
                cities = fakeCities,
                onCLickCity = {}
            )
        }

        // Проверяем, что список городов отображается
        composeTestRule.onNodeWithTag(TEST_TAG_CITY_LIST).assertIsDisplayed()

        // Проверяем, что элементы списка видны (по имени города)
        // Для этого используем onNodeWithText, так как это содержимое ячеек
        composeTestRule.onNodeWithText("Москва").assertIsDisplayed()
        composeTestRule.onNodeWithText("Санкт-Петербург").assertIsDisplayed()
    }
}

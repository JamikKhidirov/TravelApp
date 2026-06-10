import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.home.HomeScreenContent
import com.example.home.state.HomeUiState
import com.example.common.UiError
import com.example.home.state.ui.PaginationState
import org.junit.Rule
import org.junit.Test



class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_shouldShowSkeleton() {
        // Создаем стейт, где включена глобальная загрузка
        val state = HomeUiState(isGlobalLoading = true)

        composeTestRule.setContent {
            val navController = rememberNavController()
            // Замени TravelAppTheme на имя своей темы из папки ui.theme
            HomeScreenContent(
                uiState = state,
                navHostController = navController,
                onAction = {}
            )
        }

        // Проверяем, что скелетон отображается
        composeTestRule.onNodeWithTag("loading_skeleton").assertIsDisplayed()

        // Проверяем, что основной контент НЕ виден
        composeTestRule.onNodeWithTag("main_content").assertDoesNotExist()
    }

    @Test
    fun errorState_shouldShowErrorScreen() {
        // Имитируем ошибку (передай любой объект UiError, какой у тебя есть)
        val state = HomeUiState(
            error = UiError.NoInternet, // Пример, если это sealed class
            isGlobalLoading = false
        )

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreenContent(
                uiState = state,
                navHostController = navController,
                onAction = {}
            )
        }

        // Проверяем наличие экрана ошибки
        composeTestRule.onNodeWithTag("error_screen").assertIsDisplayed()

        // Проверяем, виден ли текст "Повторить" (или что у тебя в NoInternetScreen)
        composeTestRule.onNodeWithText("Повторить").assertIsDisplayed()
    }

    @Test
    fun successState_shouldShowData() {
        // Состояние, когда загрузка завершена и есть данные
        val state = HomeUiState(
            isGlobalLoading = false,
            error = null,
            citiesState = PaginationState(items = listOf(/* твои фейковые данные */))
        )

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreenContent(
                uiState = state,
                navHostController = navController,
                onAction = {}
            )
        }

        // Проверяем, что контент загрузился (например, карточка поиска)
        composeTestRule.onNodeWithTag("main_content").assertIsDisplayed()

        // Если внутри SearchCard есть текст "Поиск"
        composeTestRule.onNodeWithText("Поиск", substring = true).assertExists()
    }
}
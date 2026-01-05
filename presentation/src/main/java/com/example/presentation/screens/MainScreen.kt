package com.example.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.domain.sputnikdata.citydata.CityDto
import com.example.presentation.uicomponents.tabrow.TabRowMainScreen
import com.example.presentation.uicomponents.topbars.MainTopAppBars
import com.example.presentation.uicomponents.vidjets.CityItem
import viewmodals.MainViewModel

// Константы для тестов
const val TEST_TAG_LOADING_WHEEL = "loading_wheel"
const val TEST_TAG_ERROR_TEXT = "error_text"
const val TEST_TAG_CITY_LIST = "city_list"
const val TEST_TAG_EMPTY_TEXT = "empty_text"

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val cities by viewModel.citiesState.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // ВАЖНО: для SwipeRefresh обычно используют отдельный флаг или тот же loading
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Scaffold(
        topBar = {
            // Показываем TopBar только если нет ошибки и не идет первоначальная загрузка
            if (errorMessage == null && !isLoading) {
                MainTopAppBars()
            }
        }
    ) { paddingValues ->
        BaseScreenContainer(
            isLoading = isLoading,
            isRefreshing = isRefreshing,
            errorMessage = errorMessage,
            onRefresh = { viewModel.refresh() }, // Используем refresh() для корректной работы SwipeRefresh
            modifier = Modifier.padding(paddingValues)
        ) {
            // Если мы здесь — значит данные загружены успешно
            BottomMainScreenContent(
                cities = cities,
                onCLickCity = { /* ... */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreenContainer(
    isLoading: Boolean,
    isRefreshing: Boolean,
    errorMessage: String?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // 1. Если это первая загрузка (данных еще нет)
    if (isLoading && !isRefreshing) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .testTag(TEST_TAG_LOADING_WHEEL)
            )
        }
    }
    // 2. Если произошла ошибка
    else if (errorMessage != null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = errorMessage,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(TEST_TAG_ERROR_TEXT)
            )
        }
    }
    // 3. Основной контент с поддержкой SwipeRefresh
    else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier.fillMaxSize()
        ) {
            content()
        }
    }
}

@Composable
fun BottomMainScreenContent(
    cities: List<CityDto>,
    onCLickCity: (CityDto) -> Unit
) {
    if (cities.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Города не найдены",
                modifier = Modifier.testTag(TEST_TAG_EMPTY_TEXT)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag(TEST_TAG_CITY_LIST)
        ) {
            stickyHeader {
                TabRowMainScreen(
                    modifier = Modifier.padding(top = 2.dp),
                    onClickTab = { /* ... */ }
                )
            }
            items(cities, key = { it.id }) { city ->
                CityItem(city = city, onClickCityDto = onCLickCity)
            }
        }
    }
}

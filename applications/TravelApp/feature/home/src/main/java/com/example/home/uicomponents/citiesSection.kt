package com.example.home.uicomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.home.action.HomeAction
import com.example.home.state.HomeUiState
import com.example.network.wegodata.citiesdata.City
import com.example.uikit.uicomponents.buttons.MainButton
import com.example.uikit.uicomponents.dowloads.items.RowItemsShimmerPlaceHolder
import com.example.uikit.uicomponents.vidjets.RowCities


fun LazyListScope.citiesSection(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    val citiesState = state.citiesState

    // 1. Шиммер первоначальной загрузки
    if (citiesState.items.isEmpty() && citiesState.isLoading) {
        item(key = "cities_shimmer") {
            RowItemsShimmerPlaceHolder()
        }
    }
    // 2. Горизонтальный список городов
    else if (citiesState.items.isNotEmpty()) {
        item(key = "cities_content") {
            // Запоминаем лямбды, чтобы RowCities не перерисовывался из-за новых объектов функций
            val onClickCity = remember(onAction) {
                { city: City -> onAction(HomeAction.OnCityClick(city)) } // Укажите ваш тип вместо City
            }
            val onLoadMore = remember(onAction) {
                { onAction(HomeAction.LoadMoreCities) }
            }

            RowCities(
                results = citiesState.items,
                onClickCity = onClickCity,
                isLoading = citiesState.isLoading,
                onLoadMore = onLoadMore
            )
        }

        // 3. Кнопка вынесена в отдельный item с уникальным ключом
        item(key = "cities_main_button") {
            MainButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClickButton = { /* действие */ }
            )
        }
    }
}
package com.example.home.uicomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.home.action.HomeAction
import com.example.home.state.HomeUiState
import com.example.network.wegodata.attractiondata.Attraction
import com.example.uikit.uicomponents.buttons.MainButton
import com.example.uikit.uicomponents.dowloads.items.RowItemsShimmerPlaceHolder
import com.example.uikit.uicomponents.vidjets.RowCities


fun LazyListScope.attractionsSection(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    val attractionState = uiState.attractionState

    // 1. Заголовок
    if (attractionState.items.isNotEmpty()) {
        item(key = "attractions_header") {
            Text(
                text = "Еще популярные места",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp, start = 15.dp)
            )
        }
    }

    // 2. Первоначальный Шиммер
    if (attractionState.items.isEmpty() && attractionState.isLoading) {
        item(key = "attractions_shimmer") {
            RowItemsShimmerPlaceHolder()
        }
    }
    // 3. Список достопримечательностей
    else if (attractionState.items.isNotEmpty()) {
        item(key = "attractions_content") {
            // Запоминаем лямбды, чтобы RowCities не перерисовывался из-за смены ссылок на функции
            val onClick = remember(onAction) {
                { attraction: Attraction -> onAction(HomeAction.OnAttractionClick(attraction)) }
            }
            val onLoadMore = remember(onAction) {
                { onAction(HomeAction.LoadMoreAttractions) }
            }

            RowCities<Attraction>(
                modifier = Modifier.padding(top = 10.dp),
                results = attractionState.items,
                onClickCity = onClick,
                isLoading = attractionState.isLoading,
                onLoadMore = onLoadMore
            )
        }

        // 4. Кнопка "Показать все"
        item(key = "attractions_see_all_button") {
            val onClickButton = remember(onAction) {
                { onAction(HomeAction.SeeAllAttractions) }
            }

            MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                onClickButton = onClickButton
            )
        }
    }
}
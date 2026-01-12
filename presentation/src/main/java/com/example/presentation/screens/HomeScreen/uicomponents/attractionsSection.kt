package com.example.presentation.screens.HomeScreen.uicomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.wegodata.attractiondata.Attraction
import com.example.presentation.states.actions.HomeAction
import com.example.presentation.states.screen.HomeUiState
import com.example.presentation.uicomponents.buttons.MainButton
import com.example.presentation.uicomponents.dowloads.items.RowItemsShimmerPlaceHolder
import com.example.presentation.uicomponents.vidjets.RowCities


fun LazyListScope.attractionsSection(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    val attractionState = uiState.attractionState

    // 1. Заголовок (показываем только если есть данные)
    if (attractionState.items.isNotEmpty()) {
        item {
            Text(
                text = "Еще популярные места",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp, start = 15.dp)
            )
        }
    }

    // 2. Контент (Шиммер или Список)
    item {
        if (attractionState.items.isEmpty() && attractionState.isLoading) {
            RowItemsShimmerPlaceHolder()
        } else {
            RowCities<Attraction>( // Используем твой компонент RowCities
                modifier = Modifier.padding(top = 10.dp),
                results = attractionState.items,
                onClickCity = { attraction ->
                    onAction(HomeAction.OnAttractionClick(attraction))
                },
                isLoading = attractionState.isLoading,
                onLoadMore = { onAction(HomeAction.LoadMoreAttractions) }
            )
        }
    }

    // 3. Кнопка "Показать все"
    if (attractionState.items.isNotEmpty()) {
        item {
            MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                onClickButton = { onAction(HomeAction.SeeAllAttractions) }
            )
        }
    }
}
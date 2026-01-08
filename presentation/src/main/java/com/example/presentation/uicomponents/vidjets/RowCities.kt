package com.example.presentation.uicomponents.vidjets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.citiesdata.City
import com.example.presentation.uicomponents.vidjets.ImageVidjetDesc.ImagevidjetGetCities



@Composable
fun <T: DisplayableItem> RowCities(
    modifier: Modifier = Modifier,
    isLoading: Boolean, // Добавляем состояние загрузки
    onLoadMore: () -> Unit, // Коллбек для загрузки
    results: List<T>,
    onClickCity: (T) -> Unit
){

    val state = rememberLazyListState()

    // Триггер пагинации: если осталось 2 элемента до конца, вызываем загрузку
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= state.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !isLoading) {
            onLoadMore()
        }
    }


    LazyRow(
        modifier = modifier,
        state = state,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(start = 15.dp)
    ) {
        items(results, key = {
            it.id
        }){ city ->
            ImagevidjetGetCities(
                modifier = Modifier.animateItem(), // 🔥 ВОТ ОНО,
                displayItem = city,
                onClickCity = onClickCity
            )
        }

        // Индикатор загрузки в конце горизонтального списка
        if (isLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp).padding(8.dp),
                    strokeWidth = 2.dp,
                    color = Color.Blue
                )
            }
        }
    }
}


package com.example.uikit.uicomponents.vidjets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.model.DisplayableItem



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
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(start = 15.dp)
    ) {
        items(results, key = {
            it.id
        }){ city ->
            _root_ide_package_.com.example.uikit.uicomponents.vidjets.ImageVidjetDesc.ImagevidjetGetCities(
                modifier = Modifier.animateItem(), // 🔥 ВОТ ОНО,
                displayItem = city,
                onClickCity = onClickCity
            )
        }

        // Индикатор загрузки в конце горизонтального списка
        if (isLoading && results.isNotEmpty()) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.size(62.dp).padding(8.dp)
                        .wrapContentSize(Alignment.Center),
                    strokeWidth = 5.dp,
                    color = Color(0XFFFF8C00)
                )
            }
        }
    }
}


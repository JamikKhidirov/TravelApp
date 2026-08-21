package com.example.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun OnBottomReached(
    state: LazyListState,
    buffer: Int = 2,
    isLoading: Boolean,      // Флаг: прямо сейчас идёт загрузка
    isEndReached: Boolean,   // Флаг: данные на бекенде закончились
    onLoadMore: () -> Unit
) {
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    val shouldLoadMore = remember(state, buffer) {
        derivedStateOf {
            val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            val totalItems = state.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisibleItem.index >= totalItems - 1 - buffer
        }
    }

    // Передаем флаги в ключи LaunchedEffect или проверяем внутри snapshotFlow
    LaunchedEffect(shouldLoadMore, isLoading, isEndReached) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .collect { isReached ->
                // ВЫЗЫВАЕМ ТОЛЬКО ЕСЛИ:
                // 1. Достигли конца
                // 2. Прямо сейчас ничего не загружается
                // 3. Данные на бекенде ЕЩЁ НЕ ЗАКОНЧИЛИСЬ
                if (isReached && !isLoading && !isEndReached) {
                    currentOnLoadMore()
                }
            }
    }
}
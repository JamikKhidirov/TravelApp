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
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    // 1. Запоминаем событие с помощью rememberUpdatedState,
    // чтобы не перезапускать Effect при смене лямбды
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    // 2. Вычисляем состояние без подписки в UI-потоке
    val shouldLoadMore = remember(state, buffer) {
        derivedStateOf {
            val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            val totalItems = state.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisibleItem.index >= totalItems - 1 - buffer
        }
    }

    // 3. Запускаем эффект единожды и подписываемся на поток значений
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged() // Пропускаем только если значение реально изменилось
            .collect { isReached ->
                if (isReached) {
                    currentOnLoadMore()
                }
            }
    }
}
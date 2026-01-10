package com.example.presentation.states.ui


// Вспомогательный класс для хранения состояния конкретного списка
data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val page: Int = 1,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false
)
package com.example.home.state.ui

// Вспомогательный класс для хранения состояния конкретного списка
data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val page: Int = 1,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false
)
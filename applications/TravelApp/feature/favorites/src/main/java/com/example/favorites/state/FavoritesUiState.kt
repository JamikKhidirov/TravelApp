package com.example.favorites.state

import com.example.network.wegodata.productpopular.Tour

data class FavoritesUiState(
    val tours: List<Tour> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = true
)

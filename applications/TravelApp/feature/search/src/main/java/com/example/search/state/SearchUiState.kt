package com.example.search.state

import com.example.common.UiError
import com.example.network.wegodata.productpopular.Tour
import com.example.network.wegodata.searchdata.CityDetail

data class SearchUiState(
    val query: String = "",
    val results: List<CityDetail> = emptyList(),
    val popularTours: List<Tour> = emptyList(),
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = false,
    val error: UiError? = null
)

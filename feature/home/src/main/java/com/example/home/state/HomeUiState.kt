package com.example.home.state

import com.example.home.state.network.UiError
import com.example.home.state.ui.PaginationState
import com.example.network.wegodata.attractiondata.Attraction
import com.example.network.wegodata.citiesdata.City
import com.example.network.wegodata.productpopular.Tour

// Единый UI-state для всего экрана
data class HomeUiState(
    val citiesState: PaginationState<City> = PaginationState(),
    val attractionState: PaginationState<Attraction> = PaginationState(),
    val popularToursState: PaginationState<Tour> = PaginationState(),
    val isPopularTab: Boolean = true,

    val error: UiError? = null,
    val isGlobalLoading: Boolean = false
)
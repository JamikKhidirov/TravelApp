package com.example.presentation.states.screen

import com.example.domain.wegodata.attractiondata.Attraction
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.productpopular.Tour
import com.example.presentation.states.network.UiError
import com.example.presentation.states.ui.PaginationState



// Единый UI-state для всего экрана
data class HomeUiState(
    val citiesState: PaginationState<City> = PaginationState(),
    val attractionState: PaginationState<Attraction> = PaginationState(),
    val popularToursState: PaginationState<Tour> = PaginationState(),
    val isPopularTab: Boolean = true,

    val error: UiError? = null,
    val isGlobalLoading: Boolean = false
)

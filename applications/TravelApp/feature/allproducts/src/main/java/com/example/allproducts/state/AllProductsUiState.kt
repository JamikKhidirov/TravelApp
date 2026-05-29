package com.example.allproducts.state

import com.example.home.state.network.UiError
import com.example.home.state.ui.PaginationState
import com.example.network.wegodata.productpopular.Tour

data class AllProductsUiState(
    val toursState: PaginationState<Tour> = PaginationState(),
    val isGlobalLoading: Boolean = false,
    val error: UiError? = null
)

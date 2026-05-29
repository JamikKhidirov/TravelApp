package com.example.productdetail.state

import com.example.home.state.network.UiError
import com.example.network.wegodata.productdatailinfodata.TourFullInfo

data class DetailUiState(
    val tour: TourFullInfo? = null,
    val isLoading: Boolean = false,
    val error: UiError? = null
)

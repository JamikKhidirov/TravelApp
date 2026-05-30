package com.example.search.state

import com.example.network.wegodata.datareviews.Review
import com.example.network.wegodata.productdatailinfodata.TourFullInfo

data class DetailUiState(
    val tour: TourFullInfo? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingReviews: Boolean = false,
    val error: String? = null
)

package com.example.network.wegodata.datareviews

import androidx.annotation.Keep


@Keep
data class ReviewsData(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    val results: List<Review>
)
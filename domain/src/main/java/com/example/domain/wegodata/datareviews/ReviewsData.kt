package com.example.domain.wegodata.datareviews





data class ReviewsData(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    val results: List<Review>
)
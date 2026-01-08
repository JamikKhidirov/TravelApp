package com.example.domain.wegodata.productpopular



data class TourData(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    val results: List<Tour>,
    val maxPrice: Double
)
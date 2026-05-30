package com.example.network.wegodata.productpopular


data class TourResponse(
    val count: Int = 0,
    val pages: Int = 0,
    val current: Int = 0,
    val next: Int? = null,
    val results: List<Tour> = emptyList(),
    val maxPrice: Double = 0.0
)

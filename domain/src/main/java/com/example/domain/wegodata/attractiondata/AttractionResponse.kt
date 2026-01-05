package com.example.domain.wegodata.attractiondata






data class AttractionResponse(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    val results: List<Attraction>
)

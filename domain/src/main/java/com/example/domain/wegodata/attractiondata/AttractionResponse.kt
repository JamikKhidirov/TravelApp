package com.example.domain.wegodata.attractiondata

import com.example.domain.model.DisplayableItem


data class AttractionResponse(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    val results: List<DisplayableItem>
)

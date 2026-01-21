package com.example.network.wegodata.attractiondata

import com.example.domain.model.DisplayableItem
import com.google.gson.annotations.SerializedName


data class AttractionResponse(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    @SerializedName("results")
    val results: List<Attraction>
)

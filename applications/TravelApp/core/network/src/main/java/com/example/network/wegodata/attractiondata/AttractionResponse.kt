package com.example.network.wegodata.attractiondata

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName



@Keep
data class AttractionResponse(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    @SerializedName("results")
    val results: List<Attraction>
)

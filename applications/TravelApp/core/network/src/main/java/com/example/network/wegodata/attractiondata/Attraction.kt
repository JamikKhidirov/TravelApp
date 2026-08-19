package com.example.network.wegodata.attractiondata


import androidx.annotation.Keep
import com.example.common.model.DisplayableItem
import com.google.gson.annotations.SerializedName



@Keep
data class Attraction(
    @SerializedName("id") override val id: Int,
    @SerializedName("name") override val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("preview") override val preview: String,
    @SerializedName("itemsCount") override val itemsCount: Int
): DisplayableItem

package com.example.network.wegodata.citiesdata

import androidx.annotation.Keep
import com.example.common.model.DisplayableItem





@Keep
data class City(
    override val id: Int,
    override val name: String,
    val slug: String,
    override val preview: String,
    override val itemsCount: Int,
    val country: String
): DisplayableItem {}

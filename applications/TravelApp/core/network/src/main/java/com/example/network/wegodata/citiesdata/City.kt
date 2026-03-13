package com.example.network.wegodata.citiesdata

import com.example.common.model.DisplayableItem


data class City(
    override val id: Int,
    override val name: String,
    val slug: String,
    override val preview: String,
    override val itemsCount: Int,
    val country: String
): DisplayableItem {}

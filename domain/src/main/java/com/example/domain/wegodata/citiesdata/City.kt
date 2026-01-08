package com.example.domain.wegodata.citiesdata

import com.example.domain.model.DisplayableItem


data class City(
    override val id: Int,
    override val name: String,
    val slug: String,
    override val preview: String,
    override val itemsCount: Int,
    val country: String
): DisplayableItem{}

package com.example.domain.wegodata.attractiondata

import com.example.domain.model.DisplayableItem


data class Attraction(
    override val id: Int,
    override val name: String,
    val slug: String,
    override val preview: String,
    override val itemsCount: Int
): DisplayableItem

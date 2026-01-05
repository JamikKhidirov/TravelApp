package com.example.domain.wegodata.citiesdata




data class City(
    val id: Int,
    val name: String,
    val slug: String,
    val preview: String,
    val itemsCount: Int,
    val country: String
)

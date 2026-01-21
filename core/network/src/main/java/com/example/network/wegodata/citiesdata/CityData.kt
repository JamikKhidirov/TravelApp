package com.example.network.wegodata.citiesdata



data class CityData(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?,
    val results: List<City>
)

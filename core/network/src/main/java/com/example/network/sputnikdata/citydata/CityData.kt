package com.example.network.sputnikdata.citydata



data class CityData(
    val slug: String,
    val product: Int,
    val id: Int,
    val name_in_case: String,
    val providers: Int,
    val geo: GeoData,
    val name: String,
    val url: String
)




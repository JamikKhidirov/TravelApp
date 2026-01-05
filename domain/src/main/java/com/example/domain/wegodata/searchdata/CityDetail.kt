package com.example.domain.wegodata.searchdata




data class CityDetail(
    val id: Int,
    val name: String,
    val slug: String,
    val preview: String,
    val available: Boolean,
    val country: Country,
    val type: String
)
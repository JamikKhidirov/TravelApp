package com.example.network.wegodata.contrydata

import androidx.annotation.Keep


@Keep
data class CountryData(
    val count: Int,
    val pages: Int,
    val current: Int,
    val next: Int?, // Может быть null на последней странице
    val results: List<CountryItem>
)
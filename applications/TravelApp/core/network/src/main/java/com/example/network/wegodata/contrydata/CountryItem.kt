package com.example.network.wegodata.contrydata

import androidx.annotation.Keep


// Модель конкретной страны
@Keep
data class CountryItem(
    val id: Int,
    val code: String,
    val name: String,
    val slug: String
)
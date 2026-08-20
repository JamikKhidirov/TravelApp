package com.example.network.wegodata.contrydata

import androidx.annotation.Keep


// Основной объект ответа
@Keep
data class CountryResponse(
    val data: CountryData
)
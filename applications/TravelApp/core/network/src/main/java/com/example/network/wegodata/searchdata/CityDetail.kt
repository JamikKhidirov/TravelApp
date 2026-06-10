package com.example.network.wegodata.searchdata

data class CityDetail(
    val id: Int = 0,
    val name: String? = null,
    val title: String? = null,
    val slug: String = "",
    val preview: String = "",
    val available: Boolean? = null,
    val price: Double? = null,
    val currency: String? = null,
    val currencyCode: String? = null,
    val country: Country? = null,
    val type: String = ""
) {
    val displayName: String
        get() = name ?: title ?: ""
}
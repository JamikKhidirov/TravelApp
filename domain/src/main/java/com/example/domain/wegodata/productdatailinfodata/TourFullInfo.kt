package com.example.domain.wegodata.productdatailinfodata





data class TourFullInfo(
    val id: Int,
    val slug: String,
    val title: String,
    val cover: String,
    val preview: String,
    val images: List<TourImage>,
    val description: String,
    val highlights: List<String>,
    val distance: String,
    val duration: String,
    val address: String,
    val startLocation: String,
    val locationGeo: GeoPoint,
    val finishAddress: String,
    val finishLocation: String,
    val finishLocationGeo: GeoPoint,
    val price: Double,
    val currency: String,
    val currencyCode: String,
    val defaultCurrency: CurrencyInfo,
    val types: TourTypes,
    val inclusions: List<String>,
    val exclusions: List<String>,
    val category: String,
    val categories: List<Category>,
    val subcategories: List<Category>,
    val attractions: List<SimpleAttraction>,
    val author: AuthorInfo
)
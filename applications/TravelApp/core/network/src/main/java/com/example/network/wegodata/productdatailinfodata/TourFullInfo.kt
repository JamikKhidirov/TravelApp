package com.example.network.wegodata.productdatailinfodata

import com.example.network.wegodata.datareviews.Review

data class TourFullInfo(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
    val cover: String = "",
    val preview: String = "",
    val images: List<TourImage> = emptyList(),
    val description: String = "",
    val highlights: List<String> = emptyList(),
    val distance: String = "",
    val duration: String = "",
    val address: String = "",
    val startLocation: String = "",
    val locationGeo: GeoPoint? = null,
    val finishAddress: String = "",
    val finishLocation: String = "",
    val finishLocationGeo: GeoPoint? = null,
    val price: Double = 0.0,
    val currency: String = "",
    val currencyCode: String = "",
    val defaultCurrency: CurrencyInfo? = null,
    val types: TourTypes? = null,
    val inclusions: List<String> = emptyList(),
    val exclusions: List<String> = emptyList(),
    val category: String = "",
    val categories: List<Category> = emptyList(),
    val subcategories: List<Category> = emptyList(),
    val attractions: List<SimpleAttraction> = emptyList(),
    val author: AuthorInfo? = null,
    val reviews: List<Review> = emptyList(),
    val rating: Double? = null,
    val reviewsCount: Int = 0
)
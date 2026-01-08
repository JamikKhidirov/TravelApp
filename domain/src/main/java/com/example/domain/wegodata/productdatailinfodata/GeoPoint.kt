package com.example.domain.wegodata.productdatailinfodata





data class GeoPoint(
    val type: String,
    val coordinates: List<Double> // [lng, lat]
)
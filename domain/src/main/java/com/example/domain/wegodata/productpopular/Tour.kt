package com.example.domain.wegodata.productpopular







data class Tour(
    val id: Int,
    val title: String,
    val slug: String,
    val cover: String,
    val preview: String,
    val price: Double,
    val exprice: Double,
    val currency: String,
    val currencyCode: String,
    val rating: Double?,
    val reviewsCount: Int,
    val ratingsCount: Int,
    val category: String?,
    val city: TourCity,
    val duration: String?,
    val durationMin: Int?,
    val durationMax: Int?,
    val type: Int,
    val tags: TourTags,
    val locale: String,
    val author: TourAuthor
)
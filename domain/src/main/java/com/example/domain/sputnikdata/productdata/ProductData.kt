package com.example.domain.sputnikdata.productdata

import com.example.domain.sputnikdata.productdata.categoriproduct.CategoriProductData
import com.example.domain.sputnikdata.productdata.order.OrderData
import com.example.domain.sputnikdata.productdata.photodata.cover.CoverPhotoData
import com.example.domain.sputnikdata.productdata.hostdata.HostData
import com.example.domain.sputnikdata.productdata.photodata.main.MainPhotoData
import com.example.domain.sputnikdata.productdata.place.BeginPlaceData


data class ProductData(
    val id: Int,
    val activity_type: String,
    val title: String,
    val updated_a: String,
    val netto_price: String,
    val order_options: List<OrderData>,
    val pay_type: String,
    val pay_type_in_text: String,
    val categories: List<CategoriProductData>,
    val recommendation: Int,
    val languages: List<String>,
    val popularity: Int,
    val description: String,
    val url: String,
    val cover_photo: CoverPhotoData,
    val main_photo: MainPhotoData,
    val what_included: String,
    val what_not_included: String,
    val customers_review_rating: Int,
    val reviews: Int,
    val reviews_with_text: Int,
    val begin_place: BeginPlaceData,
    val finish_point: String,
    val minimum_book_period: Int,
    val places_to_see: String,
    val short_info: String,
    val city_id: Int,
    val city_slug: String,
    val region_id: Int,
    val country_id: Int,
    val country_slug: String,
    val price: String,
    val rating: Int,
    val image_small: String,
    val image_big: String,
    val duration: String,
    val product_type: String,
    val host: HostData
)
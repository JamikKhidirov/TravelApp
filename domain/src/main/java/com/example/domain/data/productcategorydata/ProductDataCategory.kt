package com.example.domain.data.productcategorydata

data class ProductDataCategory(
    val id: Int,
    val product: List<DescCategData>,
    val short_name: String,
    val description: String,
    val name: String,
    val url: String,
    val slug: String
)
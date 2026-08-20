package com.example.network.wegodata.datareviews

import androidx.annotation.Keep


@Keep
data class Review(
    val name: String,
    val text: String,
    val avatar: String,
    val rating: Int,
    val date: String
)
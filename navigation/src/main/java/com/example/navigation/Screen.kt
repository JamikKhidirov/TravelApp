package com.example.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class Screen {


    @Serializable
    data object Home: Screen()


    @Serializable
    data object Search: Screen()


    @Serializable
    data class Details(val itemId: String): Screen()
}
package com.example.common

import kotlinx.serialization.Serializable


@Serializable
sealed interface ScreenDestination {


    @Serializable
    data object HomeRoot: ScreenDestination

    @Serializable
    data object HomeScreen: ScreenDestination


    @Serializable
    data object SearchScreen: ScreenDestination

    @Serializable
    data object AllProducts: ScreenDestination

    @Serializable
    data class ProductDetail(val id: Int): ScreenDestination

    @Serializable
    data object FavoritesScreen: ScreenDestination

    @Serializable
    data object ProfileScreen: ScreenDestination
}
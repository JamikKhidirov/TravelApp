package com.example.presentation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenDestination {


    @Serializable
    data object HomeRoot: ScreenDestination

    @Serializable
    data object HomeScreen: ScreenDestination


    @Serializable
    data object SearchScreen: ScreenDestination
}
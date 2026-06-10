package com.example.favorites.action

sealed interface FavoritesAction {
    object Load : FavoritesAction
    object ClearAll : FavoritesAction
    data class RemoveTour(val id: Int) : FavoritesAction
    data class OnTourClick(val id: Int) : FavoritesAction
}

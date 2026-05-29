package com.example.favorites

import com.example.favorites.state.FavoritesUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesViewModelTest {

    @Test
    fun `initial state has empty tours`() {
        val state = FavoritesUiState()
        assertTrue(state.tours.isEmpty())
        assertTrue(state.isEmpty)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loading state set correctly`() {
        val state = FavoritesUiState(isLoading = true)
        assertTrue(state.isLoading)
    }

    @Test
    fun `non-empty favorites`() {
        val state = FavoritesUiState(isEmpty = false)
        assertFalse(state.isEmpty)
    }
}

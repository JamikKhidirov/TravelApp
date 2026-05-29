package com.example.allproducts

import com.example.allproducts.state.AllProductsUiState
import com.example.home.state.ui.PaginationState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AllProductsViewModelTest {

    @Test
    fun `initial state has empty tours and no loading`() {
        val state = AllProductsUiState()
        assertTrue(state.toursState.items.isEmpty())
        assertFalse(state.toursState.isLoading)
        assertEquals(1, state.toursState.page)
        assertFalse(state.isGlobalLoading)
    }

    @Test
    fun `loading state sets isLoading correctly`() {
        val state = AllProductsUiState()
        val loading = state.copy(isGlobalLoading = true)
        assertTrue(loading.isGlobalLoading)
    }

    @Test
    fun `toursState pagination increments page`() {
        val pagination = PaginationState(items = listOf(1, 2, 3), page = 2)
        val state = AllProductsUiState(toursState = pagination)
        assertEquals(2, state.toursState.page)
        assertEquals(3, state.toursState.items.size)
    }

    @Test
    fun `error state propagated correctly`() {
        val error = com.example.home.state.network.UiError.NoInternet
        val state = AllProductsUiState(error = error)
        assertTrue(state.error is com.example.home.state.network.UiError.NoInternet)
    }
}

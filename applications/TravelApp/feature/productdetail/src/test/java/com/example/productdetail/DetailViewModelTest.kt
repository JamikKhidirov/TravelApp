package com.example.productdetail

import com.example.productdetail.state.DetailUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DetailViewModelTest {

    @Test
    fun `initial state has null tour and no loading`() {
        val state = DetailUiState()
        assertNull(state.tour)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loading state set correctly`() {
        val state = DetailUiState(isLoading = true)
        assertTrue(state.isLoading)
    }

    @Test
    fun `tour loaded state`() {
        val state = DetailUiState(isLoading = false)
        assertFalse(state.isLoading)
    }

    @Test
    fun `error state`() {
        val error = com.example.home.state.network.UiError.Unknown("test error")
        val state = DetailUiState(error = error)
        assertEquals("test error", (state.error as com.example.home.state.network.UiError.Unknown).message)
    }
}

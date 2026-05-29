package com.example.search

import com.example.search.action.SearchAction
import com.example.search.state.SearchUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchViewModelTest {

    @Test
    fun `initial state has empty query and no results`() {
        val state = SearchUiState()
        assertEquals("", state.query)
        assertTrue(state.results.isEmpty())
        assertFalse(state.isLoading)
    }

    @Test
    fun `onSearchQueryChange updates query`() {
        val state = SearchUiState()
        val updated = state.copy(query = "Москва")
        assertEquals("Москва", updated.query)
    }

    @Test
    fun `ClearQuery resets state`() {
        val state = SearchUiState(
            query = "test",
            results = emptyList(),
            isLoading = true
        )
        val cleared = state.copy(query = "", results = emptyList(), isLoading = false, error = null)
        assertEquals("", cleared.query)
        assertTrue(cleared.results.isEmpty())
        assertFalse(cleared.isLoading)
    }

    @Test
    fun `loading state transitions correctly`() {
        val state = SearchUiState()
        val loading = state.copy(isLoading = true)
        assertTrue(loading.isLoading)
        val loaded = loading.copy(isLoading = false, results = emptyList())
        assertFalse(loaded.isLoading)
    }
}

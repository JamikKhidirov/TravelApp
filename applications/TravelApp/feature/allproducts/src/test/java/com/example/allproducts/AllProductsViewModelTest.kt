package com.example.allproducts

import com.example.allproducts.state.AllProductsUiState
import com.example.home.state.ui.PaginationState
import com.example.network.wegodata.citiesdata.City
import com.example.network.wegodata.productpopular.Tour
import com.example.network.wegodata.productpopular.TourAuthor
import com.example.network.wegodata.productpopular.TourTags
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AllProductsViewModelTest {

    private fun createTour(id: Int) = Tour(
        id = id,
        title = "Tour $id",
        slug = "tour-$id",
        cover = "",
        preview = "",
        price = 100.0,
        exprice = 90.0,
        currency = "USD",
        currencyCode = "USD",
        rating = 4.5,
        reviewsCount = 10,
        ratingsCount = 15,
        category = null,
        city = City(id = 1, name = "City", slug = "city", preview = "", itemsCount = 0, country = "Country"),
        duration = "2 hours",
        durationMin = 120,
        durationMax = 120,
        type = 1,
        tags = TourTags(),
        locale = "en",
        author = TourAuthor()
    )

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
        val tours = listOf(createTour(1), createTour(2), createTour(3))
        val pagination = PaginationState(items = tours, page = 2)
        val state = AllProductsUiState(toursState = pagination)
        assertEquals(2, state.toursState.page)
        assertEquals(3, state.toursState.items.size)
    }

    @Test
    fun `error state propagated correctly`() {
        val error = com.example.common.UiError.NoInternet
        val state = AllProductsUiState(error = error)
        assertTrue(state.error is com.example.common.UiError.NoInternet)
    }
}

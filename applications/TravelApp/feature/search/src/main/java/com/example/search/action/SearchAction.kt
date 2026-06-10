package com.example.search.action

import com.example.network.wegodata.searchdata.CityDetail

sealed interface SearchAction {
    data class OnSearchQueryChange(val query: String) : SearchAction
    object ClearQuery : SearchAction
    data class OnCityClick(val city: CityDetail) : SearchAction
}

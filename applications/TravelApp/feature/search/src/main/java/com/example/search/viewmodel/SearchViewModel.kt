package com.example.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.domain.tours.GetPupularProductsUseCase
import com.example.home.state.network.UiError
import com.example.search.action.SearchAction
import com.example.search.domain.tours.GetSearchUseCase
import com.example.search.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchUseCase: GetSearchUseCase,
    private val getPupularProductsUseCase: GetPupularProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPopularTours()
    }

    fun handleAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnSearchQueryChange -> {
                _uiState.update { it.copy(query = action.query) }
                searchJob?.cancel()
                if (action.query.isNotEmpty()) {
                    searchJob = viewModelScope.launch {
                        delay(400)
                        search(action.query)
                    }
                } else {
                    _uiState.update { it.copy(results = emptyList(), isLoading = false, error = null) }
                }
            }
            SearchAction.ClearQuery -> {
                _uiState.update { it.copy(query = "", results = emptyList(), isLoading = false, error = null) }
            }
            is SearchAction.OnCityClick -> {}
        }
    }

    private suspend fun search(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            val response = getSearchUseCase(query = query)
            if (response.isSuccessful) {
                val results = response.body()?.data?.results ?: emptyList()
                _uiState.update { it.copy(results = results, isLoading = false, error = null) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = UiError.Unknown("Ошибка: ${response.code()}")) }
            }
        } catch (e: Exception) {
            val error = when (e) {
                is java.net.UnknownHostException, is java.net.ConnectException, is java.io.IOException -> UiError.NoInternet
                else -> UiError.Unknown(e.message)
            }
            _uiState.update { it.copy(isLoading = false, error = error) }
        }
    }

    private fun loadPopularTours() {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }
            try {
                val response = getPupularProductsUseCase(page = 1, popularity = "popularity")
                if (response.isSuccessful) {
                    val tours = response.body()?.data?.results ?: emptyList()
                    _uiState.update { it.copy(popularTours = tours, isInitialLoading = false) }
                } else {
                    _uiState.update { it.copy(isInitialLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isInitialLoading = false) }
            }
        }
    }
}

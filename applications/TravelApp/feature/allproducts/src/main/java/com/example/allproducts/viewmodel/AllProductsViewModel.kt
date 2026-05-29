package com.example.allproducts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allproducts.action.AllProductsAction
import com.example.allproducts.domain.tours.GetAllProductsUseCase
import com.example.allproducts.state.AllProductsUiState
import com.example.home.state.network.UiError
import com.example.home.state.ui.PaginationState
import com.example.network.wegodata.productpopular.TourResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllProductsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGlobalLoading = true, error = null) }
            loadTours()
            _uiState.update { it.copy(isGlobalLoading = false) }
        }
    }

    fun handleAction(action: AllProductsAction) {
        when (action) {
            AllProductsAction.LoadMore -> viewModelScope.launch { loadTours() }
            AllProductsAction.Retry -> initialLoad()
            is AllProductsAction.OnTourClick -> {}
        }
    }

    private suspend fun loadTours() {
        val current = _uiState.value.toursState
        if (current.isLoading || current.isEndReached) return

        _uiState.update { state ->
            state.copy(toursState = state.toursState.copy(isLoading = true, error = null))
        }

        try {
            val response = getAllProductsUseCase(page = current.page)
            if (response.isSuccessful) {
                val newItems = response.body()?.data?.results ?: emptyList()
                _uiState.update { state ->
                    val pState = state.toursState
                    state.copy(toursState = pState.copy(
                        items = pState.items + newItems,
                        page = pState.page + 1,
                        isLoading = false,
                        isEndReached = newItems.isEmpty(),
                        error = null
                    ))
                }
            } else {
                _uiState.update { state ->
                    state.copy(toursState = state.toursState.copy(
                        error = UiError.Unknown("Ошибка: ${response.code()}"),
                        isLoading = false
                    ))
                }
            }
        } catch (e: Exception) {
            val error = when (e) {
                is java.net.UnknownHostException, is java.net.ConnectException, is java.io.IOException -> UiError.NoInternet
                else -> UiError.Unknown(e.message)
            }
            _uiState.update { state ->
                state.copy(toursState = state.toursState.copy(error = error, isLoading = false))
            }
        }
    }
}

package com.example.productdetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.UiError
import com.example.productdetail.action.DetailAction
import com.example.productdetail.domain.tours.GetProductDetailUseCase
import com.example.productdetail.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductDetailUseCase: GetProductDetailUseCase
) : ViewModel() {

    private val productId: Int = savedStateHandle.get<Int>("id") ?: 0

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun handleAction(action: DetailAction) {
        when (action) {
            DetailAction.Retry -> loadDetail()
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = getProductDetailUseCase(id = productId)
                if (response.isSuccessful) {
                    val tour = response.body()?.data
                    _uiState.update { it.copy(tour = tour, isLoading = false) }
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
    }
}

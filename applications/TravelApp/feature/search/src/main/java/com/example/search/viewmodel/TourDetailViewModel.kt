package com.example.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search.domain.TourDetailUseCase
import com.example.search.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourDetailViewModel @Inject constructor(
    private val tourDetailUseCase: TourDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tourId: Int = savedStateHandle["tourId"] ?: -1

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        if (tourId > 0) {
            loadTourDetail()
            loadReviews()
        }
    }

    fun retry() {
        loadTourDetail()
        loadReviews()
    }

    private fun loadTourDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = tourDetailUseCase.getTourDetail(tourId)
                if (response.isSuccessful) {
                    val tour = response.body()?.data
                    _uiState.update { it.copy(tour = tour, isLoading = false) }
                } else {
                    _uiState.update { it.copy(error = "Ошибка загрузки", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingReviews = true) }
            try {
                val response = tourDetailUseCase.getReviews(tourId)
                if (response.isSuccessful) {
                    val reviews = response.body()?.data?.results ?: emptyList()
                    _uiState.update { it.copy(reviews = reviews, isLoadingReviews = false) }
                } else {
                    _uiState.update { it.copy(isLoadingReviews = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingReviews = false) }
            }
        }
    }
}

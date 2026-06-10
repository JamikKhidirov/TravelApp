package com.example.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.favorites.action.FavoritesAction
import com.example.favorites.state.FavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        handleAction(FavoritesAction.Load)
    }

    fun handleAction(action: FavoritesAction) {
        when (action) {
            FavoritesAction.Load -> load()
            FavoritesAction.ClearAll -> clearAll()
            is FavoritesAction.RemoveTour -> removeTour(action.id)
            is FavoritesAction.OnTourClick -> {}
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // TODO: load from local DB when implemented
            _uiState.update { it.copy(isLoading = false, isEmpty = true) }
        }
    }

    private fun clearAll() {
        _uiState.update { FavoritesUiState() }
    }

    private fun removeTour(id: Int) {
        _uiState.update { state ->
            val updated = state.tours.filter { it.id != id }
            state.copy(tours = updated, isEmpty = updated.isEmpty())
        }
    }
}

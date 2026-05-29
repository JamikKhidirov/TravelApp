package com.example.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.action.HomeAction
import com.example.home.domain.tours.GetListAttractionUseCase
import com.example.home.domain.tours.GetListCitiesUseCase
import com.example.home.domain.tours.GetPupularProductsUseCase
import com.example.home.state.HomeUiState
import com.example.common.UiError
import com.example.home.state.ui.PaginationState
import com.example.location.domain.LocationClient
import com.example.network.setvice.WegoExcursionService
import com.example.network.setvice.WegoExcursionServiveV3
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.attractiondata.AttractionResponse
import com.example.network.wegodata.citiesdata.CityResponse
import com.example.network.wegodata.productpopular.TourResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject




@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val getListAttractionUseCase: GetListAttractionUseCase,
    private val getListCitiesUseCase: GetListCitiesUseCase,
    private val getPupularProductsUseCase: GetPupularProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            // Сбрасываем старую ошибку и включаем лоадер
            _uiState.update { it.copy(isGlobalLoading = true, error = null) }

            // Запускаем параллельно
            val cities = launch { loadCities() }
            val attractions = launch { loadAttractions() }
            val tours = launch { loadTours() }

            joinAll(cities, attractions, tours)

            _uiState.update { it.copy(isGlobalLoading = false) }
        }
    }

    fun handleAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadMoreCities -> viewModelScope.launch { loadCities() }
            HomeAction.LoadMoreAttractions -> viewModelScope.launch { loadAttractions() }
            HomeAction.LoadMoreTours -> viewModelScope.launch { loadTours() }

            HomeAction.Retry -> {
                // Полный перезапуск данных
                initialLoad()
            }

            is HomeAction.ChangeTab -> {
                _uiState.update {
                    it.copy(
                        isPopularTab = action.isPopular,
                        citiesState = PaginationState(),
                        error = null // Сбрасываем ошибку при смене таба
                    )
                }
                viewModelScope.launch { loadCities() }
            }

            is HomeAction.OnCityClick -> { /* Navigation */ }
            is HomeAction.OnAttractionClick -> { /* Navigation */ }
            is HomeAction.OnTourClick -> { /* Navigation */ }
            HomeAction.SeeAllAttractions -> { /* Navigation */ }
        }
    }

    private suspend fun <T> executeLoadInternal(
        stateSelector: (HomeUiState) -> PaginationState<T>,
        updateState: (HomeUiState, PaginationState<T>) -> HomeUiState,
        call: suspend (Int) -> Response<*>
    ) {
        val currentPagination = stateSelector(_uiState.value)
        if (currentPagination.isLoading || currentPagination.isEndReached) return

        _uiState.update { state ->
            val pState = stateSelector(state).copy(isLoading = true, error = null)  // ← Сброс локальной ошибки
            updateState(state, pState)
        }

        try {
            val response = call(currentPagination.page)
            if (response.isSuccessful) {
                val newItems = extractList<T>(response)
                _uiState.update { state ->
                    val pState = stateSelector(state)
                    updateState(state, pState.copy(
                        items = pState.items + newItems,
                        page = pState.page + 1,
                        isLoading = false,
                        isEndReached = newItems.isEmpty(),
                        error = null  // ← Сброс при успехе
                    ))
                }
            } else {
                // ← ЛОКАЛЬНАЯ ошибка!
                _uiState.update { state ->
                    val pState = stateSelector(state).copy(
                        error = UiError.Unknown("Ошибка сервера: ${response.code()}"),
                        isLoading = false
                    )
                    updateState(state, pState)
                }
            }
        } catch (e: Exception) {
            val errorType = when (e) {
                is java.net.UnknownHostException,
                is java.net.ConnectException,
                is java.io.IOException -> UiError.NoInternet
                else -> UiError.Unknown(e.message)
            }
            // ← ЛОКАЛЬНАЯ ошибка!
            _uiState.update { state ->
                val pState = stateSelector(state).copy(
                    error = errorType,
                    isLoading = false
                )
                updateState(state, pState)
            }
        }
    }

    private suspend fun loadCities() = executeLoadInternal(
        stateSelector = { it.citiesState },
        updateState = { old, new -> old.copy(citiesState = new) }
    ) { page -> getListCitiesUseCase(page = page, popular = _uiState.value.isPopularTab) }

    private suspend fun loadAttractions() = executeLoadInternal(
        stateSelector = { it.attractionState },
        updateState = { old, new -> old.copy(attractionState = new) }
    ) { page -> getListAttractionUseCase(page = page) }

    private suspend fun loadTours() = executeLoadInternal(
        stateSelector = { it.popularToursState },
        updateState = { old, new -> old.copy(popularToursState = new) }
    ) { page -> getPupularProductsUseCase(page = page, country = null, attraction = null, popularity = "popularity") }

    @Suppress("UNCHECKED_CAST")
    private fun <T> extractList(response: Response<*>): List<T> {
        val body = response.body() ?: return emptyList()
        return when (body) {
            is CityResponse -> body.data.results as List<T>
            is AttractionResponse -> body.results as List<T>
            is TourResponse -> body.data.results as List<T>
            else -> emptyList()
        }
    }
}
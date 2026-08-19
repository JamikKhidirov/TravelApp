package com.example.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.action.HomeAction
import com.example.home.domain.tours.GetListAttractionUseCase
import com.example.home.domain.tours.GetListCitiesUseCase
import com.example.home.domain.tours.GetPupularProductsUseCase
import com.example.home.state.HomeUiState
import com.example.home.state.network.UiError
import com.example.home.state.ui.PaginationState
import com.example.location.domain.LocationClient
import com.example.network.setvice.WegoExcursionService
import com.example.network.setvice.WegoExcursionServiveV3
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.attractiondata.Attraction
import com.example.network.wegodata.attractiondata.AttractionResponse
import com.example.network.wegodata.citiesdata.City
import com.example.network.wegodata.citiesdata.CityResponse
import com.example.network.wegodata.productpopular.Tour
import com.example.network.wegodata.productpopular.TourResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class HomeViewModel @Inject constructor(
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
            _uiState.update {
                HomeUiState(isGlobalLoading = true)
            }

            try {
                coroutineScope {
                    val citiesDeferred = async { fetchCities(1, _uiState.value.isPopularTab) }
                    val attractionsDeferred = async { fetchAttractions(1) }
                    val toursDeferred = async { fetchTours(1) }

                    val cities = citiesDeferred.await()
                    val attractions = attractionsDeferred.await()
                    val tours = toursDeferred.await()

                    _uiState.update { state ->
                        state.copy(
                            isGlobalLoading = false,
                            citiesState = PaginationState(items = cities, page = 2),
                            attractionState = PaginationState(items = attractions, page = 2),
                            popularToursState = PaginationState(items = tours, page = 2)
                        )
                    }
                }
            } catch (e: CancellationException) {
                // Пробрасываем корутинную отмену дальше, не считаем её ошибкой сети
                throw e
            } catch (e: Exception) {
                val errorType = parseError(e)
                _uiState.update { state ->
                    state.copy(
                        isGlobalLoading = false,
                        citiesState = state.citiesState.copy(error = errorType)
                    )
                }
            }
        }
    }

    private suspend fun fetchCities(page: Int, isPopular: Boolean): List<City> {
        val response = getListCitiesUseCase(page = page, popular = isPopular)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.data?.results ?: emptyList()
    }

    private suspend fun fetchAttractions(page: Int): List<Attraction> {
        val response = getListAttractionUseCase(page = page)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.results ?: emptyList()
    }

    private suspend fun fetchTours(page: Int): List<Tour> {
        val response = getPupularProductsUseCase(page = page, country = null, attraction = null, popularity = "popularity")
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.data?.results ?: emptyList()
    }

    fun handleAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadMoreCities -> viewModelScope.launch { loadMoreCitiesInternal() }
            HomeAction.LoadMoreAttractions -> viewModelScope.launch { loadMoreAttractionsInternal() }
            HomeAction.LoadMoreTours -> viewModelScope.launch { loadMoreToursInternal() }

            HomeAction.Retry -> initialLoad()

            is HomeAction.ChangeTab -> {
                _uiState.update {
                    it.copy(
                        isPopularTab = action.isPopular,
                        citiesState = PaginationState() // Сброс списка городов
                    )
                }
                viewModelScope.launch { loadMoreCitiesInternal() }
            }

            is HomeAction.OnCityClick -> { /* Navigation */ }
            is HomeAction.OnAttractionClick -> { /* Navigation */ }
            is HomeAction.OnTourClick -> { /* Navigation */ }
            HomeAction.SeeAllAttractions -> { /* Navigation */ }
        }
    }

    private suspend fun loadMoreCitiesInternal() {
        executePagingLoad(
            stateSelector = { it.citiesState },
            updateState = { old, new -> old.copy(citiesState = new) },
            call = { page -> getListCitiesUseCase(page = page, popular = _uiState.value.isPopularTab) }
        )
    }

    private suspend fun loadMoreAttractionsInternal() {
        executePagingLoad(
            stateSelector = { it.attractionState },
            updateState = { old, new -> old.copy(attractionState = new) },
            call = { page -> getListAttractionUseCase(page = page) }
        )
    }

    private suspend fun loadMoreToursInternal() {
        executePagingLoad(
            stateSelector = { it.popularToursState },
            updateState = { old, new -> old.copy(popularToursState = new) },
            call = { page -> getPupularProductsUseCase(page = page, country = null, attraction = null, popularity = "popularity") }
        )
    }

    private suspend fun <T> executePagingLoad(
        stateSelector: (HomeUiState) -> PaginationState<T>,
        updateState: (HomeUiState, PaginationState<T>) -> HomeUiState,
        call: suspend (Int) -> Response<*>
    ) {
        val currentPagination = stateSelector(_uiState.value)
        if (currentPagination.isLoading || currentPagination.isEndReached) return

        _uiState.update { state ->
            val pState = stateSelector(state).copy(isLoading = true, error = null)
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
                        error = null
                    ))
                }
            } else {
                _uiState.update { state ->
                    val pState = stateSelector(state).copy(
                        error = UiError.Unknown("Ошибка сервера: ${response.code()}"),
                        isLoading = false
                    )
                    updateState(state, pState)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _uiState.update { state ->
                val pState = stateSelector(state).copy(
                    error = parseError(e),
                    isLoading = false
                )
                updateState(state, pState)
            }
        }
    }

    // Расширенная обработка ошибок HTTP и сети
    private fun parseError(e: Throwable): UiError = when (e) {
        is java.net.UnknownHostException,
        is java.net.ConnectException,
        is java.net.SocketTimeoutException,
        is IOException -> UiError.NoInternet

        is HttpException -> {
            when (e.code()) {
                in 500..599 -> UiError.Unknown("Ошибка на сервере (${e.code()})")
                else -> UiError.Unknown("Ошибка сети: ${e.code()}")
            }
        }
        else -> UiError.Unknown(e.message ?: "Неизвестная ошибка")
    }

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
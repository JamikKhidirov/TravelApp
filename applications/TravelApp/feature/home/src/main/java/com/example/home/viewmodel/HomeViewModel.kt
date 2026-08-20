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
            _uiState.update { HomeUiState(isGlobalLoading = true) }

            try {
                // Выполняем параллельно, но обрабатываем ошибки каждого запроса отдельно
                val citiesDef = async { runCatching { fetchCities(1, _uiState.value.isPopularTab) } }
                val attractionsDef = async { runCatching { fetchAttractions(1) } }
                val toursDef = async { runCatching { fetchTours(1) } }

                val citiesRes = citiesDef.await().getOrDefault(emptyList())
                val attractionsRes = attractionsDef.await().getOrDefault(emptyList())
                val toursRes = toursDef.await().getOrDefault(emptyList())

                _uiState.update { state ->
                    state.copy(
                        isGlobalLoading = false,
                        citiesState = PaginationState(items = citiesRes, page = 2),
                        attractionState = PaginationState(items = attractionsRes, page = 2),
                        popularToursState = PaginationState(items = toursRes, page = 2)
                    )
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { it.copy(isGlobalLoading = false) }
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
                        citiesState = PaginationState()
                    )
                }
                viewModelScope.launch { loadMoreCitiesInternal() }
            }
            else -> {}
        }
    }

    private suspend fun loadMoreCitiesInternal() {
        executePagingLoad(
            stateSelector = { it.citiesState },
            updateState = { old, new -> old.copy(citiesState = new) },
            fetch = { page -> fetchCities(page, _uiState.value.isPopularTab) }
        )
    }

    private suspend fun loadMoreAttractionsInternal() {
        executePagingLoad(
            stateSelector = { it.attractionState },
            updateState = { old, new -> old.copy(attractionState = new) },
            fetch = { page -> fetchAttractions(page) }
        )
    }

    private suspend fun loadMoreToursInternal() {
        executePagingLoad(
            stateSelector = { it.popularToursState },
            updateState = { old, new -> old.copy(popularToursState = new) },
            fetch = { page -> fetchTours(page) }
        )
    }

    // Безопасная типобезопасная пагинация без Unchecked Cast
    private suspend fun <T> executePagingLoad(
        stateSelector: (HomeUiState) -> PaginationState<T>,
        updateState: (HomeUiState, PaginationState<T>) -> HomeUiState,
        fetch: suspend (page: Int) -> List<T>
    ) {
        val currentPagination = stateSelector(_uiState.value)

        // Жесткий блок от повторных вызовов
        if (currentPagination.isLoading || currentPagination.isEndReached) return

        _uiState.update { state ->
            val pState = stateSelector(state).copy(isLoading = true, error = null)
            updateState(state, pState)
        }

        try {
            val newItems = fetch(currentPagination.page)

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
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            _uiState.update { state ->
                val pState = stateSelector(state).copy(
                    error = parseError(e),
                    isLoading = false
                )
                updateState(state, pState)
            }
        }
    }

    private fun parseError(e: Throwable): UiError = when (e) {
        is IOException -> UiError.NoInternet
        is HttpException -> UiError.Unknown("Ошибка сети: ${e.code()}")
        else -> UiError.Unknown(e.message ?: "Неизвестная ошибка")
    }
}
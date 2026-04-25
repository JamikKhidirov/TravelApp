package com.example.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.action.HomeAction
import com.example.home.state.HomeUiState
import com.example.home.state.network.UiError
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
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService,
    @WeGoApi(WeGo.ATTRACTION) private val attractionApi: WegoExcursionServiveV3,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialLoad()
    }



    // 1. ПАРАЛЛЕЛЬНАЯ ЗАГРУЗКА ВСЕГО
    private fun initialLoad() {
        viewModelScope.launch {
            // Включаем общий лоадер перед стартом всех задач
            _uiState.update { it.copy(isGlobalLoading = true, error = null) }

            // Запускаем 3 дочерние корутины параллельно
            val cities = launch { loadCities() }
            val attractions = launch { loadAttractions() }
            val tours = launch { loadTours() }

            // Ждем, пока ВСЕ ТРИ завершатся (успешно или с ошибкой)
            joinAll(cities, attractions, tours)

            // Выключаем лоадер только когда всё закончилось
            _uiState.update { it.copy(isGlobalLoading = false) }
        }
    }

    // 4. ОБРАБОТКА ЭКШЕНОВ (теперь проще)
    fun handleAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadMoreCities -> viewModelScope.launch { loadCities() }
            HomeAction.LoadMoreAttractions -> viewModelScope.launch { loadAttractions() }
            HomeAction.LoadMoreTours -> viewModelScope.launch { loadTours() }
            HomeAction.Retry -> initialLoad()
            is HomeAction.ChangeTab -> {
                _uiState.update { it.copy(isPopularTab = action.isPopular, citiesState = PaginationState()) }
                viewModelScope.launch { loadCities() }
            }

            is HomeAction.OnCityClick -> {
                // Здесь будет: navigationController.navigate("city/${action.city.id}")
            }

            is HomeAction.OnAttractionClick -> {
                //Открытие экрана аттракционов
            }
            is HomeAction.OnTourClick -> {
                //Открытие экрана Туров
            }
            HomeAction.SeeAllAttractions -> {
                //Открытие экрана всех атракционов обработка нажаитя на кноку
            }

            HomeAction.Retry -> {
                _uiState.update { it.copy(error = null) }

                handleAction(HomeAction.LoadMoreCities)
                handleAction(HomeAction.LoadMoreAttractions)
                handleAction(HomeAction.LoadMoreTours)
            }
        }
    }

    //
    private suspend fun <T> executeLoadInternal(
        stateSelector: (HomeUiState) -> PaginationState<T>,
        updateState: (HomeUiState, PaginationState<T>) -> HomeUiState,
        call: suspend (Int) -> Response<*>
    ) {
        // Берем текущее состояние пагинации
        val currentPagination = stateSelector(_uiState.value)
        if (currentPagination.isLoading || currentPagination.isEndReached) return

        // Помечаем, что конкретно эта секция начала грузиться
        _uiState.update { state ->
            val pState = stateSelector(state)
            updateState(state, pState.copy(isLoading = true))
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
                        isEndReached = newItems.isEmpty()
                    ))
                }
            } else {
                throw Exception("API Error")
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = UiError.Unknown(e.message)) }
            // Не забываем сбросить флаг загрузки даже при ошибке
            _uiState.update { state ->
                val pState = stateSelector(state)
                updateState(state, pState.copy(isLoading = false))
            }
        }
    }

    // 3. ОБЕРТКИ ДЛЯ КОНКРЕТНЫХ ЗАПРОСОВ
    private suspend fun loadCities() = executeLoadInternal(
        stateSelector = { it.citiesState },
        updateState = { old, new -> old.copy(citiesState = new) }
    ) { page -> api.getListCities(page = page, popular = _uiState.value.isPopularTab) }

    private suspend fun loadAttractions() = executeLoadInternal(
        stateSelector = { it.attractionState },
        updateState = { old, new -> old.copy(attractionState = new) }
    ) { page -> attractionApi.getListattraction(page = page) }

    private suspend fun loadTours() = executeLoadInternal(
        stateSelector = { it.popularToursState },
        updateState = { old, new -> old.copy(popularToursState = new) }
    ) { page -> api.getPopularProducts(page = page, attraction = null, country = null, popularity = "popularity") }



    // Пример того, как мы "выпрямляем" разные ответы API
    private fun <T> extractList(response: Response<*>): List<T> {
        val body = response.body() ?: return emptyList()

        return when (body) {
            is CityResponse -> body.data.results as List<T>  // Берем из data.results
            is AttractionResponse -> body.results as List<T> // Берем напрямую из results
            is TourResponse -> body.data.results as List<T>
            else -> emptyList()
        }
    }


}
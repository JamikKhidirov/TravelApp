package viewmodals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.attractiondata.Attraction
import com.example.domain.wegodata.attractiondata.AttractionResponse
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.citiesdata.CityResponse
import com.example.domain.wegodata.productpopular.Tour
import com.example.domain.wegodata.productpopular.TourResponse
import com.example.network.setvice.WegoExcursionService
import com.example.network.setvice.WegoExcursionServiveV3
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.presentation.states.actions.HomeAction
import com.example.presentation.states.network.UiError
import com.example.presentation.states.screen.HomeUiState
import com.example.presentation.states.ui.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class HomeViewModel @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService,
    @WeGoApi(WeGo.ATTRACTION) private val attractionApi: WegoExcursionServiveV3
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {


        // Первичная загрузка
        handleAction(HomeAction.LoadMoreCities)
        handleAction(HomeAction.LoadMoreAttractions)
        handleAction(HomeAction.LoadMoreTours)
    }

    fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.ChangeTab -> {
                // Сбрасываем только города при смене таба
                _uiState.update { it.copy(
                    isPopularTab = action.isPopular,
                    citiesState = PaginationState()
                ) }
                handleAction(HomeAction.LoadMoreCities)
            }
            HomeAction.LoadMoreCities -> {
                executeLoad(
                    stateSelector = { it.citiesState },
                    updateState = { old, new -> old.copy(citiesState = new) }
                ) { page -> api.getListCities(page = page, popular = _uiState.value.isPopularTab) }
            }
            HomeAction.LoadMoreAttractions -> {
                executeLoad(
                    stateSelector = { it.attractionState },
                    updateState = { old, new -> old.copy(attractionState = new) }
                ) { page -> attractionApi.getListattraction(page = page) }
            }
            HomeAction.LoadMoreTours -> {
                executeLoad(
                    stateSelector = { it.popularToursState },
                    updateState = { old, new -> old.copy(popularToursState = new) }
                ) { page -> api.getPopularProducts(page = page, attraction = null, country = null, popularity = "popularity") }
            }

            is HomeAction.OnCityClick -> {
                // Здесь будет: navigationController.navigate("city/${action.city.id}")
            }

            is HomeAction.OnAttractionClick -> {
                //Открываение экрана аттракционов
            }
            is HomeAction.OnTourClick -> {
                //Открывание экрана Туров
            }
            HomeAction.SeeAllAttractions -> {
                //Открывание экрана всех атракционов обработка нажаитя на кноку
            }

            HomeAction.Retry -> {
                _uiState.update { it.copy(error = null) }

                handleAction(HomeAction.LoadMoreCities)
                handleAction(HomeAction.LoadMoreAttractions)
                handleAction(HomeAction.LoadMoreTours)
            }
        }
    }

    // ТА САМАЯ УНИВЕРСАЛЬНАЯ ФУНКЦИЯ
    private fun <T> executeLoad(
        stateSelector: (HomeUiState) -> PaginationState<T>,
        updateState: (HomeUiState, PaginationState<T>) -> HomeUiState,
        call: suspend (Int) -> Response<*>
    ) {
        val currentPagination = stateSelector(_uiState.value)
        if (currentPagination.isLoading || currentPagination.isEndReached) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGlobalLoading = true,
                    error = null
                )
            }

            try {
                val response = call(currentPagination.page)

                if (response.isSuccessful) {
                    val newItems = extractList<T>(response)

                    _uiState.update { state ->
                        updateState(
                            state.copy(isGlobalLoading = false),
                            currentPagination.copy(
                                items = currentPagination.items + newItems,
                                page = currentPagination.page + 1,
                                isLoading = false,
                                isEndReached = newItems.isEmpty()
                            )
                        )
                    }
                } else {
                    throw Exception("Server error")
                }

            } catch (e: IOException) {
                //НЕТ ИНТЕРНЕТА
                _uiState.update {
                    it.copy(
                        isGlobalLoading = false,
                        error = UiError.NoInternet
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isGlobalLoading = false,
                        error = UiError.Unknown(e.message)
                    )
                }
            }
        }
    }



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


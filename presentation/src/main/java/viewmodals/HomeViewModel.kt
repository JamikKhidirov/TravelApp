package viewmodals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.attractiondata.Attraction
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.citiesdata.CityResponse
import com.example.domain.wegodata.productpopular.Tour
import com.example.network.setvice.WegoExcursionService
import com.example.network.setvice.WegoExcursionServiveV3
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class HomeViewModel @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService,
    @WeGoApi(WeGo.ATTRACTION) private val attractionApi: WegoExcursionServiveV3
) : ViewModel() {

    private val _popular = MutableStateFlow(true)
    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities
    private val _isNextCitiesPageLoading = MutableStateFlow(false)
    val isNextCitiesPageLoading: StateFlow<Boolean> = _isNextCitiesPageLoading.asStateFlow()
    private var citiesPage: Int = 1
    private var isEndReachedCities = false



    private var _attractionList:
            MutableStateFlow<List<Attraction>> = MutableStateFlow(emptyList())
    val attractionList = _attractionList.asStateFlow()
    private val _isNextAttractionPageLoading = MutableStateFlow(false)
    val isNextAttractionPageLoading: StateFlow<Boolean> = _isNextAttractionPageLoading.asStateFlow()
    private var attractionPage: Int = 1
    private var isEndReachedAttraction = false



    private val _popularTours: MutableStateFlow<List<Tour>> = MutableStateFlow(emptyList())
    val popularTours: StateFlow<List<Tour>> = _popularTours.asStateFlow()
    private val _isNextPopularPageLoading = MutableStateFlow(false)
    val isNextPopularPageLoading: StateFlow<Boolean> = _isNextPopularPageLoading.asStateFlow()
    private var popularToursPage: Int = 1
    private var isEndReachedPopularTours = false




    init {
        _popular
            .onEach { popular ->
                loadCities(popular)
            }
            .launchIn(viewModelScope)

        loadAttreaction()

        loadPopular()
    }

    fun loadCities(
        popular: Boolean
    ) {
        // Если уже грузим или данные закончились — выходим
        if (_isNextCitiesPageLoading.value || isEndReachedCities) return
        viewModelScope.launch {
            _isNextCitiesPageLoading.value = true
            try {
                val response = api.getListCities(
                    popular = popular,
                    page = citiesPage + 1
                )
                if (response.isSuccessful) {
                    val newItems = response.body()?.data?.results.orEmpty()
                    if (newItems.isNotEmpty()) isEndReachedCities = true

                    else {
                        _cities.value += cities.value + newItems
                    }
                    citiesPage++
                }
            } catch (e: Exception) {
                Log.e("API", e.message ?: "Неизвестная ошибка")
            }
            finally {
                _isNextCitiesPageLoading.value = false
            }
        }
    }

    fun loadAttreaction(){
        if (_isNextCitiesPageLoading.value || isEndReachedCities) return
        viewModelScope.launch {
            _isNextAttractionPageLoading.value = true
            try {
                val response = attractionApi.getListattraction(
                    page = attractionPage + 1
                )
                if (response.isSuccessful){
                    val newItems = response.body()?.results.orEmpty()
                    if (newItems.isEmpty()){
                        isEndReachedAttraction = true
                    }
                    else{
                        _attractionList.value += _attractionList.value + newItems
                        attractionPage++
                    }
                }
                else{
                    Log.e("API", "Ошибка сервера: ${response.code()}")
                }
            }
            catch (e: Exception){
                Log.d("API", e.message ?: "Неизвестная ошибка")
            }
            finally {
                _isNextAttractionPageLoading.value = false
            }
        }
    }


    fun loadPopular(
        page: Int? = null,
        lang: String? = null,
        currency: String = "RUB",
        country: Int? = null,
        city: Int? = null,
        attraction: Int? = null,
        order: String = "popularity"
    ){
        if (_isNextPopularPageLoading.value || isEndReachedPopularTours) return
        viewModelScope.launch {
            _isNextPopularPageLoading.value = true
            try {
                val response = api.getPopularProducts(
                    page = page,
                    lang = lang,
                    currency = currency,
                    country = country,
                    city = city,
                    attraction = attraction,
                    popularity = order
                )
                if (response.isSuccessful) {
                    val newItems = response.body()?.data?.results.orEmpty()
                    if (newItems.isEmpty()){
                        isEndReachedPopularTours = true
                    }
                    else{
                        _popularTours.value +=  _popularTours.value + newItems
                        popularToursPage++
                    }
                }
            }
            catch (e: Exception){
                Log.d("API", "Ошибка: ${e.message}")
            }
            finally {
                _isNextPopularPageLoading.value = false
            }
        }
    }

    fun setPopular(value: Boolean) {
        _popular.value = value
    }
}


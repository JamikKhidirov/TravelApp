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


    private var _attractionList:
            MutableStateFlow<List<Attraction>> = MutableStateFlow(emptyList())

    val attractionList = _attractionList.asStateFlow()



    private val _popularTours: MutableStateFlow<List<Tour>> = MutableStateFlow(emptyList())
    val popularTours: StateFlow<List<Tour>> = _popularTours.asStateFlow()




    init {
        _popular
            .onEach { popular ->
                loadCities(popular)
            }
            .launchIn(viewModelScope)

        loadAttreaction()

        loadPopular()
    }

    private fun loadCities(popular: Boolean) {
        viewModelScope.launch {
            try {
                val response = api.getListCities(popular = popular)
                if (response.isSuccessful) {
                    _cities.value = response.body()?.data?.results.orEmpty()
                }
            } catch (e: Exception) {
                Log.e("API", e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun loadAttreaction(){
        viewModelScope.launch {
            try {
                val response = attractionApi.getListattraction()

                if (response.isSuccessful){
                    _attractionList.value = response.body()?.results ?: emptyList()
                    // ЛОГ 1: Проверка размера списка из API
                    Log.d("DEBUG_DATA", "API Attractions success: size = ${response.body()?.results}")
                    // Этот лог покажет, пришел ли вообще объект ответа
                    Log.d("DEBUG_DATA", "Raw Body: ${response.body()}")

                }
                else{
                    Log.e("API", "Ошибка сервера: ${response.code()}")
                }
            }
            catch (e: Exception){
                Log.d("API", e.message ?: "Неизвестная ошибка")
                // Это выведет ПОЛНУЮ ошибку. Посмотрите её в Logcat.
                // Там будет написано что-то вроде "Expected BEGIN_ARRAY but was STRING" или "Malformed JSON"
                Log.e("API_DEBUG", "Ошибка парсинга или сети", e)
            }
        }
    }


    fun loadPopular(
        page: Int = 1,
        lang: String = "ru",
        currency: String = "RUB",
        country: Int = 1,
        city: Int? = null,
        attraction: Int = 3,
        order: String = "popularity"
    ){
        viewModelScope.launch {
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

                Log.d("API", "${response.body()?.data?.results?.size}")
                if (response.isSuccessful) {
                    Log.d("API", "${response.body()?.data?.results}")
                    response.body()?.data?.results?.let {
                        _popularTours.value = it
                    }
                }
            }
            catch (e: Exception){
                Log.d("API", "Ошибка: ${e.message}")
            }
        }
    }

    fun setPopular(value: Boolean) {
        _popular.value = value
    }
}


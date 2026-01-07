package viewmodals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.citiesdata.CityResponse
import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGoApi
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._com_example_network_di_NetworkApiModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject
import kotlin.collections.emptyList



@HiltViewModel
class HomeViewModel @Inject constructor(
    @WeGoApi private val api: WegoExcursionService
): ViewModel() {

    private var _citiesList: MutableStateFlow<List<City>> = MutableStateFlow(emptyList())
    val cities: StateFlow<List<City>> = _citiesList.asStateFlow()




    init {
        fetchCoties()
    }



    fun fetchCoties(
        page: Int = 1,
        lang: String= "ru",
        country: Int = 0,
        popular: Boolean = true
    ){
        viewModelScope.launch {
            try {
                val response = api.getListCities(
                    page = page,
                    lang = lang,
                    country = country,
                    popular = popular
                )

                if (response.isSuccessful){
                    response.body()?.data?.results?.let { list ->
                        _citiesList.value = list
                    } ?: Log.d("API", "Body or data is null")
                }
                else{
                    //Можно перехватьтить ошибку тут
                    Log.e("API", "Response error: ${response.code()}")
                }
            }
            catch (e: Exception){
                Log.e("API", "Exception: ${e.localizedMessage}")
            }


        }
    }


    override fun onCleared() {
        super.onCleared()
        //Для очистки

    }
}



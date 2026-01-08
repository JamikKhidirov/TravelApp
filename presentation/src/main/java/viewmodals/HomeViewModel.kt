package viewmodals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.citiesdata.CityResponse
import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGoApi
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._com_example_network_di_NetworkApiModule
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
    @WeGoApi private val api: WegoExcursionService
) : ViewModel() {

    private val _popular = MutableStateFlow(true)
    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities


    private var _attractionList:
            MutableStateFlow<List<DisplayableItem>> = MutableStateFlow(emptyList())

    val attractionList = _attractionList.asStateFlow()


    init {
        _popular
            .onEach { popular ->
                loadCities(popular)
            }
            .launchIn(viewModelScope)

        loadAttreaction()
    }

    private fun loadCities(popular: Boolean) {
        viewModelScope.launch {
            try {
                val response = api.getListCities(popular = popular)
                if (response.isSuccessful) {
                    _cities.value = response.body()?.data?.results.orEmpty()
                }
            } catch (e: Exception) {
                Log.e("API", e.message ?: "error")
            }
        }
    }

    fun loadAttreaction(){
        viewModelScope.launch {
            try {
                val response = api.getListattraction()

                if (response.isSuccessful){
                    _attractionList.value = response.body()?.results!!
                }
            }
            catch (e: Exception){
                Log.d("API", e.message ?: "Ошибка неизветсная")
            }
        }
    }

    fun setPopular(value: Boolean) {
        _popular.value = value
    }
}


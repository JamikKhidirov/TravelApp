package viewmodals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cache.datacache.dao.CityDao
import com.example.data.ApiRepositoryImpl
import com.example.domain.data.citydata.CityDto
import com.example.domain.state.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModal @Inject constructor(
    private val repository: ApiRepositoryImpl,
    private val dao: CityDao
): ViewModel() {

    private val _citiesState =
        MutableStateFlow<List<CityDto>>(emptyList())

    val citiesState: StateFlow<List<CityDto>> =
        _citiesState.asStateFlow()

    val errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    val loading: MutableStateFlow<Boolean> = MutableStateFlow(false)





    init {
        getCities()
    }

    fun getCities(){
        viewModelScope.launch {
            loading.value = true

            when(val result = repository.getCities()){

                is NetworkResult.Success -> {
                    val cities = result.data
                    _citiesState.value = cities
                    loading.value = false

                }

                is NetworkResult.Error -> {
                    val errorMessageR = result.message
                    errorMessage.value = errorMessageR
                    loading.value = false

                }

                is NetworkResult.Loading -> {
                    loading.value = true
                }

            }
        }
    }
}
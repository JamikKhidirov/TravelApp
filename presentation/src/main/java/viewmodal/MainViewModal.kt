package viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ApiRepositoryImpl
import com.example.domain.data.citydata.CityDto
import com.example.domain.state.NetworkResult
import com.example.network.setvice.ExcursionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModal @Inject constructor(
    private val repository: ApiRepositoryImpl
): ViewModel() {

    private val _citiesState =
        MutableStateFlow<NetworkResult<List<CityDto>>>(NetworkResult.Loading)

    val citiesState: StateFlow<NetworkResult<List<CityDto>>> =
        _citiesState.asStateFlow()

    init {
        getCities()
    }

    fun getCities(){
        viewModelScope.launch {
            _citiesState.value = NetworkResult.Loading
            _citiesState.value = repository.getCities()
        }
    }
}
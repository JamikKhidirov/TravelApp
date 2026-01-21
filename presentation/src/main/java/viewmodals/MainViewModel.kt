package viewmodals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cache.datacache.dao.CityDao
import com.example.data.ApiRepositoryImpl
import com.example.domain.state.NetworkResult
import com.example.network.sputnikdata.citydata.CityDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor( // Исправил опечатку в названии
    private val repository: ApiRepositoryImpl,
    private val dao: CityDao
) : ViewModel() {

    private val _citiesState = MutableStateFlow<List<CityDto>>(emptyList())
    val citiesState: StateFlow<List<CityDto>> = _citiesState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Основной лоадер (блокирует экран)
    private val _isLoading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Лоадер для SwipeRefresh (индикатор сверху)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getCities()
    }

    // Эта функция вызывается при Swipe-to-refresh
    fun refresh() {
        getCities(isSwipeRefresh = true)
    }

    // Универсальная функция загрузки
    fun getCities(isSwipeRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isSwipeRefresh) {
                _isRefreshing.value = true
            } else {
                _isLoading.value = true
            }

            _errorMessage.value = null // Сбрасываем ошибку перед новой попыткой

            when (val result = repository.getCities()) {
                is NetworkResult.Success -> {
                    _citiesState.value = result.data ?: emptyList()
                    // После успеха сохраняем в БД, если нужно
                }
                is NetworkResult.Error -> {
                    // Показываем ошибку только если список пуст
                    if (_citiesState.value.isEmpty()) {
                        _errorMessage.value = result.message
                    }
                }
                is NetworkResult.Loading -> {
                    // Обычно это обрабатывается через isLoading выше
                }
            }

            _isLoading.value = false
            _isRefreshing.value = false
        }
    }

    fun refreshData(){
        getCities()
    }
}
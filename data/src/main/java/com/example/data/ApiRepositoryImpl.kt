package com.example.data


import com.example.cache.datacache.dao.CityDao
import com.example.cache.datacache.data.toCityDto
import com.example.cache.datacache.data.toCityEntity
import com.example.domain.data.citydata.CityDto
import com.example.domain.state.NetworkResult
import com.example.network.setvice.ExcursionService
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiRepositoryImpl @Inject constructor(
    private val api: ExcursionService,
    private val dao: CityDao
){

    suspend fun getCities(): NetworkResult<List<CityDto>> {
        return try {
            val response = api.getCities()

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!

                // Сохраняем свежие данные в кэш
                dao.clear()
                dao.insertAll(body.map { it.toCityEntity() })

                NetworkResult.Success(body)
            } else {
                // Если сервер ответил ошибкой, берем из кэша
                getCitiesFromCache("Ошибка сервера: ${response.message()}")
            }

        } catch (e: Exception) {
            // Если нет интернета (Exception), берем из кэша
            getCitiesFromCache(e.localizedMessage ?: "Ошибка сети")
        }
    }

    private suspend fun getCitiesFromCache(errorMessage: String): NetworkResult<List<CityDto>> {
        val cachedEntities = dao.getAll()

        return if (cachedEntities.isNotEmpty()) {
            // Если в базе что-то есть, отдаем это
            val cachedDto = cachedEntities.map { it.toCityDto() }
            NetworkResult.Success(cachedDto)
        } else {
            // Если и в базе пусто, тогда уже возвращаем ошибку
            NetworkResult.Error(errorMessage)
        }
    }



}
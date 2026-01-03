package com.example.data


import com.example.domain.data.citydata.CityDto
import com.example.domain.state.NetworkResult
import com.example.network.setvice.ExcursionService
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiRepositoryImpl @Inject constructor(
    private val api: ExcursionService
){


    suspend fun getCities(): NetworkResult<List<CityDto>> {
        return try {
            val response = api.getCities()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    NetworkResult.Error("Пустой ответ сервера")
                }
            } else {
                NetworkResult.Error(
                    message = response.message(),
                    code = response.code()
                )
            }

        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.localizedMessage ?: "Ошибка сети"
            )
        }
    }


}
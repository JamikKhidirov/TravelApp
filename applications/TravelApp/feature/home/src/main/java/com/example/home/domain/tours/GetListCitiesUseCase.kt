package com.example.home.domain.tours

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.citiesdata.CityResponse
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetListCitiesUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES)
    private val api: WegoExcursionService
) {

    suspend operator fun invoke(
        page: Int,
        country: Int = 0,
        popular: Boolean,
    ): Response<CityResponse> {

        repeat(3) { attempt ->

            try {

                val response = api.getListCities(
                    page = page,
                    country = country,
                    popular = popular
                )

                // Успешный запрос
                if (response.isSuccessful) {
                    return response
                }

                // Если ошибка НЕ серверная — нет смысла повторять
                if (response.code() < 500) {
                    return response
                }

            } catch (e: Exception) {

                // Последняя попытка
                if (attempt == 2) {
                    throw e
                }
            }

            // Задержка перед повтором
            delay(1000L * (attempt + 1))
        }

        throw Exception("Request failed after 3 attempts")
    }
}
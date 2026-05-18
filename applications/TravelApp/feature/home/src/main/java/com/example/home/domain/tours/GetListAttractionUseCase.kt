package com.example.home.domain.tours

import com.example.network.setvice.WegoExcursionServiveV3
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.attractiondata.AttractionResponse
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class GetListAttractionUseCase @Inject constructor(
    @WeGoApi(WeGo.ATTRACTION)
    private val attractionApi: WegoExcursionServiveV3
) {

    suspend operator fun invoke(
        page: Int,
        country: Int = 3017382,
        city: Int = 3,
        popular: Boolean = true
    ): Response<AttractionResponse> {

        repeat(3) { attempt ->

            try {

                val response = attractionApi.getListattraction(
                    page = page,
                    country = country,
                    city = city,
                    popular = popular
                )

                // Если запрос успешный → сразу возвращаем
                if (response.isSuccessful) {
                    return response
                }

            } catch (e: Exception) {

                // Если это последняя попытка → пробрасываем ошибку
                if (attempt == 2) {
                    throw e
                }
            }

            // Небольшая задержка перед повтором
            delay(1000)
        }

        throw Exception("Request failed after 3 attempts")
    }
}
package com.example.home.domain.tours

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.productpopular.TourResponse
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Singleton




@Singleton
class GetPupularProductsUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES)
    private val api: WegoExcursionService
) {

    suspend operator fun invoke(
        page: Int,
        attraction: Int? = null,
        country: Int? = null,
        popularity: String?
    ): Response<TourResponse> {

        repeat(3) { attempt ->

            try {

                val response = api.getPopularProducts(
                    page = page,
                    attraction = attraction,
                    country = country,
                    popularity = popularity
                )

                // Успешный ответ
                if (response.isSuccessful) {
                    return response
                }

                // Не повторяем клиентские ошибки
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
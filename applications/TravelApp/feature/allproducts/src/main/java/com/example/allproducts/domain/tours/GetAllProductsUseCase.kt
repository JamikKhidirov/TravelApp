package com.example.allproducts.domain.tours

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.productpopular.TourResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllProductsUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService
) {
    suspend operator fun invoke(
        page: Int,
        country: Int? = null,
        city: Int? = null,
        attraction: Int? = null
    ): Response<TourResponse> {
        return api.getPopularProducts(
            page = page,
            country = country,
            city = city,
            attraction = attraction,
            popularity = "popularity"
        )
    }
}

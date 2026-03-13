package com.example.home.domain

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.citiesdata.CityResponse
import com.example.network.wegodata.productpopular.TourResponse
import jakarta.inject.Inject
import retrofit2.Response
import javax.inject.Singleton




@Singleton
class GetPupularProductsUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService
){
    suspend operator fun invoke(
        page: Int,
        attraction: Int? = null,
        country: Int? = null,
        popularity: String?
    ): Response<TourResponse> {

        return api.getPopularProducts(
            page = page,
            attraction = attraction,
            country = country,
            popularity = popularity
        )

    }
}
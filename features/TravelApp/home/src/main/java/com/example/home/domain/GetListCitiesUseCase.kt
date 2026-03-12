package com.example.home.domain

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.citiesdata.CityResponse
import com.example.network.wegodata.searchdata.Country
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetListCitiesUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService
) {
    suspend operator fun invoke(
        page: Int,
        country: Int,
        popular: Boolean,
    ): Response<CityResponse>
    {
        return api.getListCities(
            page = page,
            country = country,
            popular = popular
        )

    }
}
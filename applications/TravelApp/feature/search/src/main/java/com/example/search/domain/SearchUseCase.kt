package com.example.search.domain

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.citiesdata.CityResponse
import com.example.network.wegodata.searchdata.CityDetailResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService
) {
    suspend fun search(query: String): Response<CityDetailResponse> {
        return api.searchList(query = query)
    }

    suspend fun getPopularCities(page: Int = 1): Response<CityResponse> {
        return api.getListCities(page = page, popular = true)
    }
}

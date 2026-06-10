package com.example.search.domain.tours

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.searchdata.CityDetailResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService
) {
    suspend operator fun invoke(
        query: String,
        currency: String = "RUB"
    ): Response<CityDetailResponse> {
        return api.searchList(query = query, code = currency)
    }
}

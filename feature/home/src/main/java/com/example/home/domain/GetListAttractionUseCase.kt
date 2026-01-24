package com.example.home.domain

import com.example.network.setvice.WegoExcursionServiveV3
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.attractiondata.AttractionResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class GetListAttractionUseCase @Inject constructor(
    @WeGoApi(WeGo.ATTRACTION) private val attractionApi: WegoExcursionServiveV3
) {
    suspend operator fun invoke(
        page: Int,
        country: Int = 3017382,
        city: Int = 3,
        popular: Boolean = true
    ): Response<AttractionResponse> {

        return attractionApi.getListattraction(
            page = page,
            country = country,
            city = city,
            popular = popular
        )
    }
}
package com.example.productdetail.domain.tours

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.productdatailinfodata.TourDetailResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProductDetailUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService
) {
    suspend operator fun invoke(
        id: Int,
        currency: String = "RUB"
    ): Response<TourDetailResponse> {
        return api.getInfoProducts(id = id, currency = currency)
    }
}

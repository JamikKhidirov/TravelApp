package com.example.search.domain

import com.example.network.setvice.WegoExcursionService
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import com.example.network.wegodata.datareviews.ReviewsResponse
import com.example.network.wegodata.productdatailinfodata.TourDetailResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourDetailUseCase @Inject constructor(
    @WeGoApi(WeGo.CITIES) private val api: WegoExcursionService
) {
    suspend fun getTourDetail(tourId: Int): Response<TourDetailResponse> {
        return api.getInfoProducts(id = tourId)
    }

    suspend fun getReviews(tourId: Int, page: Int = 1): Response<ReviewsResponse> {
        return api.getReviews(id = tourId, page = page)
    }
}

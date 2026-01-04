package com.example.network.setvice

import com.example.domain.data.citydata.CityData
import com.example.domain.data.citydata.CityDto
import com.example.domain.data.countridata.CountriData
import com.example.domain.data.productcategorydata.ProductDataCategory
import com.example.domain.data.productdata.ProductData
import  retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ExcursionService {

    @GET("cities")
    suspend fun getCities():Response<List<CityDto>>


    @GET("cities/{id}")
    suspend fun getCitiesByID(
        @Path("id") id: Int,
        @Query("lang") lang: String = "ru"
    ): Response<CityData>


    @GET("countries")
    suspend fun getCountries(): Response<List<CountriData>>


    @GET("categories")
    suspend fun getCategories(): Response<List<ProductDataCategory>>

    @GET("product")
    suspend fun getExcursion(): Response<List<ProductData>>

}

package com.example.network.setvice

import com.example.domain.data.citydata.CityDto
import com.example.domain.data.countridata.CountriData
import com.example.domain.data.productcategorydata.ProductDataCategory
import com.example.domain.data.productdata.ProductData
import retrofit2.http.GET
import retrofit2.http.Query


interface ExcursionService {

    @GET("cities")
    suspend fun getCities(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<CityDto>


    @GET("countries")
    suspend fun getCountries(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<CountriData>


    @GET("categories")
    suspend fun getCategories(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<ProductDataCategory>

    @GET("product")
    suspend fun getExcursion(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<ProductData>

}

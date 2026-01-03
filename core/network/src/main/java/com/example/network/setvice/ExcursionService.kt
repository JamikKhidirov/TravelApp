package com.example.network.setvice

import com.example.network.data.citydata.CityDto
import com.example.network.data.countridata.CountriData
import com.example.network.data.productdata.ProductData
import com.example.network.data.productcategorydata.ProductDataCategory
import retrofit2.http.GET
import retrofit2.http.Query


interface ExcursionService {

    @GET("cities")
    suspend fun getCities(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<CityDto>


    @GET("countries")
    fun getCountries(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<CountriData>


    @GET("categories")
    fun getCategories(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<ProductDataCategory>

    @GET("product")
    fun getExcursion(
        @Query("api_key") key: String = "9bc84ec26f47bf3005dc55434b4b796a",
        @Query("username") user: String = "partners+tpo50@sputnik8.com"
    ): List<ProductData>

}

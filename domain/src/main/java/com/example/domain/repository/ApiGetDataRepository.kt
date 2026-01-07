package com.example.domain.repository

import com.example.domain.sputnikdata.citydata.CityDto
import com.example.domain.sputnikdata.productcategorydata.ProductDataCategory
import com.example.domain.sputnikdata.productdata.ProductData


interface ApiGetDataRepository {

    suspend fun getCities(): List<CityDto>

    suspend fun getCountries(): List<CityDto>

    suspend fun getCategories(): List<ProductDataCategory>


    suspend fun getExcursion(): List<ProductData>

}
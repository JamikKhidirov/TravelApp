package com.example.data

import com.example.domain.data.citydata.CityDto
import com.example.domain.data.productcategorydata.ProductDataCategory
import com.example.domain.data.productdata.ProductData
import com.example.domain.repository.ApiGetDataRepository
import com.example.network.setvice.ExcursionService
import javax.inject.Inject


class ApiRepositoryImpl @Inject constructor(
    private val api: ExcursionService
): ApiGetDataRepository {

    override suspend fun getCities(): List<CityDto> {
       return api.getCities()
    }

    override suspend fun getCountries(): List<CityDto> {
        return api.getCities()
    }

    override suspend fun getCategories(): List<ProductDataCategory> {
        return api.getCategories()
    }

    override suspend fun getExcursion(): List<ProductData> {
       return api.getExcursion()
    }

}

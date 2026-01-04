package com.example.domain.repository




interface CityRepository {
    suspend fun getCities(forceRefresh: Boolean = false): List<Any>
}
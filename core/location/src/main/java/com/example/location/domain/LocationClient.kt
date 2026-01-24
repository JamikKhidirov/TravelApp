package com.example.location.domain

import android.location.Location
import kotlinx.coroutines.flow.Flow


interface LocationClient {


    suspend fun getCurrentlocation(): Location


    fun getLocationUpdates(intervalMillis: Long): Flow<Location?>
}
package com.example.location.domain.usecases

import android.location.Location
import com.example.location.domain.LocationClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton


@Suppress("UNCHECKED_CAST")
@Singleton
class GetLocationUpdates @Inject constructor(
    private val locationClient: LocationClient
) {
    suspend operator fun invoke(
        intervalMillis: Long
    ): Flow<Location> {

       return locationClient.getLocationUpdates(
           intervalMillis = intervalMillis
       ).filterNotNull()
        
    }
}
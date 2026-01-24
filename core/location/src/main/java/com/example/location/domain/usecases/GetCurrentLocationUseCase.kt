package com.example.location.domain.usecases

import android.location.Location
import com.example.location.domain.LocationClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCurrentLocationUseCase @Inject constructor(
    private val locationClient: LocationClient
) {

    suspend operator fun invoke(): Location {
        return locationClient.getCurrentlocation()
    }
}
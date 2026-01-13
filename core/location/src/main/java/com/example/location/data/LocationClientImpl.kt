package com.example.location.data

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.example.location.domain.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



@Singleton
class LocationClientImpl @Inject constructor(
    private val fusedClient: FusedLocationProviderClient
): LocationClient {



    @SuppressLint("MissingPermission")
    override suspend fun getCurrentlocation(): Location {
        return try {
            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null,
            ).await()
        } catch (e: Exception){
            null
        } as Location
    }


    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(intervalMillis: Long): Flow<Location?> = callbackFlow {

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            intervalMillis
        ).apply {
            setMinUpdateDistanceMeters(2.0f) //метод для обновления через какое смешение будет обновляться 3 метра
        }.build()

        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.lastLocation?.let { location ->
                    // Отправляем локацию в поток
                    trySend(location)
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }
        }

        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e)
        }

        awaitClose {
            fusedClient.removeLocationUpdates(locationCallback)
        }
    }

}
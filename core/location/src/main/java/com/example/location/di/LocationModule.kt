package com.example.location.di

import android.content.Context
import com.example.location.data.LocationClientImpl
import com.example.location.domain.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {


    @Provides
    @Singleton
    fun provideFocusedLocationClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


    @Provides
    @Singleton
    fun provideLocationClient(
        fusedClient: FusedLocationProviderClient
    ): LocationClient = LocationClientImpl(fusedClient)

}
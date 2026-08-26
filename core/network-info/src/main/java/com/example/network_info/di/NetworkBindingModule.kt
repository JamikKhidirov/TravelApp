package com.example.network_info.di

import com.example.network_info.data.AndroidNetworkDataSource
import com.example.network_info.data.NetworkRepositoryImpl
import com.example.network_info.domain.repository.NetworkDataSource
import com.example.network_info.domain.repository.NetworkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindingModule {

    @Binds
    @Singleton
    abstract fun bindNetworkDataSource(
        impl: AndroidNetworkDataSource
    ): NetworkDataSource

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(
        impl: NetworkRepositoryImpl
    ): NetworkRepository
}
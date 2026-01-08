package com.example.network.di

import com.example.network.setvice.SputnikExcursionService
import com.example.network.setvice.WegoExcursionService
import com.example.network.setvice.WegoExcursionServiveV3
import com.example.network.state.SputNikApi
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {


    @Provides
    @Singleton
    @SputNikApi
    fun provideExcursionService(
        @SputNikApi
        retrofit: Retrofit
    ): SputnikExcursionService{
        return retrofit.create(SputnikExcursionService::class.java)
    }


    @Provides
    @Singleton
    @WeGoApi(WeGo.CITIES)
    fun provideWegoExcursionService(
        @WeGoApi(WeGo.CITIES)
        retrofit: Retrofit
    ): WegoExcursionService{
        return retrofit.create(WegoExcursionService::class.java)
    }

    @Provides
    @Singleton
    @WeGoApi(WeGo.ATTRACTION)
    fun provideWegoExcursionServiceV3(
        @WeGoApi(WeGo.ATTRACTION)
        retrofit: Retrofit
    ): WegoExcursionServiveV3 {
        return retrofit.create(WegoExcursionServiveV3::class.java)
    }

}
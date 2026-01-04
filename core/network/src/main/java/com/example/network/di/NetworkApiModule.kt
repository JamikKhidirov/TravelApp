package com.example.network.di

import com.example.network.setvice.SputnikExcursionService
import com.example.network.state.SputNikApi
import com.example.network.state.WeGoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
    @WeGoApi
    fun provideWegoExcursionService(
        @WeGoApi
        retrofit: Retrofit
    ){

    }

}
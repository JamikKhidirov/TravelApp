package com.example.network.di

import com.example.network.interceptors.AuthWeGoInterceptor
import com.example.network.state.SputNikApi
import com.example.network.state.WeGoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkRetrofiModule {


    @Provides
    @Singleton
    @SputNikApi
    fun provideRetrofitSputnikApi(
        @SputNikApi
        okHttpClient: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.sputnik8.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    @WeGoApi
    fun provideRetrofitWeGo(
        @WeGoApi
        okHttpClient: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://app.wegotrip.com/api/v3")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
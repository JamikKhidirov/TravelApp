package com.example.network.di

import com.example.network.interceptors.AuthSputnikInterceptor
import com.example.network.interceptors.AuthWeGoInterceptor
import com.example.network.state.SputNikApi
import com.example.network.state.WeGo
import com.example.network.state.WeGoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkOkhttpModule {


    @Provides
    @Singleton
    @SputNikApi
    fun provideSputnikOkhttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(
                AuthSputnikInterceptor(
                    apiKey = "9bc84ec26f47bf3005dc55434b4b796a",
                    username = "partners+tpo50@sputnik8.com"
                )
            )
            .build()
    }


    @Provides
    @Singleton
    @WeGoApi(WeGo.CITIES)
    fun provideWegoApiOkhttp(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthWeGoInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}
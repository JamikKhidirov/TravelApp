package com.example.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (
    private val apiKey: String,
    private val username: String
): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val urlWithParams = originalUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .addQueryParameter("username", username)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlWithParams)
            .build()

        return chain.proceed(newRequest)
    }

}
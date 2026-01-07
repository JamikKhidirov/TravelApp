package com.example.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response


class AuthWeGoInterceptor(): Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val reques = chain.request()


        val newRequest = reques.newBuilder()
            .addHeader("Accept-Language", "ru")
            .build()

        return chain.proceed(newRequest)
    }

}
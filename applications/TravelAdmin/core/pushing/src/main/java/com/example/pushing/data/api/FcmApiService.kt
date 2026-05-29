package com.example.pushing.data.api

import com.example.pushing.data.model.FcmSendRequest
import com.example.pushing.data.model.FcmSendResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApiService {

    @POST("fcm/send")
    suspend fun sendNotification(
        @Header("Authorization") authorization: String,
        @Body request: FcmSendRequest
    ): Response<FcmSendResponse>
}

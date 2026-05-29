package com.example.pushing.domain

import com.example.pushing.data.api.FcmApiService
import com.example.pushing.data.model.FcmNotification
import com.example.pushing.data.model.FcmSendRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendPushUseCase @Inject constructor(
    private val fcmApiService: FcmApiService
) {
    suspend operator fun invoke(
        serverKey: String,
        title: String,
        body: String,
        token: String? = null
    ): FcmPushResult {
        val to = if (token.isNullOrBlank()) "/topics/all" else token

        val request = FcmSendRequest(
            to = to,
            notification = FcmNotification(title = title, body = body)
        )

        return try {
            val response = fcmApiService.sendNotification(
                authorization = "key=$serverKey",
                request = request
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success > 0) {
                    FcmPushResult.Success
                } else {
                    val errorMsg = body?.results?.firstOrNull()?.error ?: "Unknown error"
                    FcmPushResult.Error(errorMsg)
                }
            } else {
                FcmPushResult.Error("Ошибка сервера: ${response.code()}")
            }
        } catch (e: Exception) {
            FcmPushResult.Error(e.message ?: "Ошибка соединения")
        }
    }
}

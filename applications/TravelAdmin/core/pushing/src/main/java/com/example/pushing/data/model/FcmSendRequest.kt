package com.example.pushing.data.model

data class FcmSendRequest(
    val to: String,
    val notification: FcmNotification,
    val data: Map<String, String>? = null
)

data class FcmNotification(
    val title: String,
    val body: String
)

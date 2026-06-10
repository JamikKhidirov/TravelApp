package com.example.notification.state

data class NotificationUiState(
    val serverKey: String = "",
    val title: String = "",
    val body: String = "",
    val token: String = "",
    val sendToAll: Boolean = true,
    val isSending: Boolean = false,
    val resultMessage: String? = null,
    val isSuccess: Boolean? = null
)

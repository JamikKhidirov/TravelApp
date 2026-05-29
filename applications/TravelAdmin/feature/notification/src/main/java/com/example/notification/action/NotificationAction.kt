package com.example.notification.action

sealed interface NotificationAction {
    data class OnServerKeyChange(val key: String) : NotificationAction
    data class OnTitleChange(val title: String) : NotificationAction
    data class OnBodyChange(val body: String) : NotificationAction
    data class OnTokenChange(val token: String) : NotificationAction
    data class OnSendToAllChange(val sendToAll: Boolean) : NotificationAction
    object Send : NotificationAction
    object ClearResult : NotificationAction
}

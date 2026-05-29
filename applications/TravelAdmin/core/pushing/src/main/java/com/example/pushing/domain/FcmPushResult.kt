package com.example.pushing.domain

sealed class FcmPushResult {
    data object Success : FcmPushResult()
    data class Error(val message: String) : FcmPushResult()
}

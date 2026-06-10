package com.example.common

sealed class UiError {
    object NoInternet : UiError()
    data class Unknown(val message: String? = null) : UiError()
}

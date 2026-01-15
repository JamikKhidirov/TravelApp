package com.example.presentation.states.network




sealed class UiError {
    object NoInternet : UiError()
    data class Unknown(val message: String? = null) : UiError()
}

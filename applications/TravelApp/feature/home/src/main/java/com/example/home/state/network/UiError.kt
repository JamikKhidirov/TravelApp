package com.example.home.state.network




sealed class UiError {
    object NoInternet : UiError()
    data class Unknown(val message: String? = null) : UiError()
}

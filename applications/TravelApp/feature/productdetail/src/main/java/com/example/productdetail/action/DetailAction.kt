package com.example.productdetail.action

sealed interface DetailAction {
    object Retry : DetailAction
}

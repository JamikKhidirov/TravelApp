package com.example.network.state

import jakarta.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeGoApi(
    val type: WeGo // Изменили имя параметра и типа
)


package com.example.network_info.data



data class WifiDetails(
    val ssid: String,
    val rssi: Int,           // Сигнал в dBm (например, -60)
    val linkSpeedMbps: Int,  // Скорость подключения
    val frequencyMhz: Int    // Частота (2412 = 2.4GHz, 5180 = 5GHz)
)

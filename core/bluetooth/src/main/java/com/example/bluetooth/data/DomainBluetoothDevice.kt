package com.example.bluetooth.data




data class DomainBluetoothDevice(
    val name: String?,
    val address: String?,
    val rssi: Int? = null
)
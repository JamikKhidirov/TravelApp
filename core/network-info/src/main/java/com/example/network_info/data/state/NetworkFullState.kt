package com.example.network_info.data.state

import com.example.network_info.data.CellularDetails
import com.example.network_info.data.WifiDetails




data class NetworkFullState(
    val isConnected: Boolean = false,
    val primaryType: NetworkType = NetworkType.NO_INTERNET,
    val isMetered: Boolean = false,       // Лимитное ли подключение
    val isVpnActive: Boolean = false,
    val wifiDetails: WifiDetails? = null,
    val cellularDetails: CellularDetails? = null
)

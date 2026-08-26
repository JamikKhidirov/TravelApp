package com.example.network_info.data



data class CellularDetails(
    val operatorName: String,
    val networkType: String, // 2G, 3G, 4G, 5G
    val simState: String,
    val isRoaming: Boolean
)

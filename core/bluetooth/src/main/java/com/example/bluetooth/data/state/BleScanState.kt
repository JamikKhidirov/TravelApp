package com.example.bluetooth.data.state




sealed interface BleScanState{
    object Idle : BleScanState
    object Scanning : BleScanState
    data class Error(val message: String) : BleScanState
}
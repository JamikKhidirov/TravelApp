package com.example.bluetooth.domain

import com.example.bluetooth.data.DomainBluetoothDevice
import com.example.bluetooth.data.state.BleScanState
import kotlinx.coroutines.flow.StateFlow

interface BluetoothManager {

    val scannedDevices: StateFlow<List<DomainBluetoothDevice>>
    val scanState: StateFlow<BleScanState>

    fun startScan()
    fun stopScan()
    fun connect(address: String)
    fun disconnect()
}
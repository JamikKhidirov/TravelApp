package com.example.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import androidx.annotation.RequiresPermission
import com.example.bluetooth.data.state.BleScanState
import com.example.bluetooth.domain.BluetoothManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidBluetoothManagerImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    ): BluetoothManager{

    private val _scannedDevices = MutableStateFlow<List<DomainBluetoothDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<DomainBluetoothDevice>> = _scannedDevices.asStateFlow()

    private val scanner get() = bluetoothAdapter?.bluetoothLeScanner

    private val _scanState = MutableStateFlow<BleScanState>(BleScanState.Idle)
    override val scanState: StateFlow<BleScanState>
        get() = _scanState.asStateFlow()


    private val scanCallback = object: ScanCallback(){
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            val device = DomainBluetoothDevice(
                name = result?.device?.name ?: result?.scanRecord?.deviceName,
                address = result?.device?.address,
                rssi = result?.rssi
            )
            _scannedDevices.update { current ->
                if (current.none { it.address == device.address }) current + device else current
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            _scanState.value = BleScanState.Error("Scan failed with code: $errorCode")
        }

    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun startScan() {
        if (scanner == null) return
        _scannedDevices.value = emptyList()
        _scanState.value = BleScanState.Scanning
        scanner?.startScan(scanCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun stopScan() {
        scanner?.stopScan(scanCallback)
        _scanState.value = BleScanState.Idle
    }

    override fun connect(address: String) {
        TODO("Not yet implemented")
        //Тут логика подключения к устройствам
    }

    override fun disconnect() {
        TODO("Not yet implemented")
        //Тут логика короче отключения
    }




}
package com.example.network_info.data

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import com.example.network_info.data.state.NetworkFullState
import com.example.network_info.data.state.NetworkType
import com.example.network_info.domain.repository.NetworkDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class AndroidNetworkDataSource @Inject constructor(
    private val connectiviManager: ConnectivityManager,
    private val telephonyManager: TelephonyManager,
    private val wifiManager: WifiManager
): NetworkDataSource {



    override fun getCurrentState(): NetworkFullState {
        val activeNetwork: Network? = connectiviManager.activeNetwork
        val capabilities: NetworkCapabilities? =
            connectiviManager.getNetworkCapabilities(activeNetwork)

        if (activeNetwork == null || capabilities == null) {
            return NetworkFullState(isConnected = false, primaryType = NetworkType.NO_INTERNET)
        }

        val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        val isVpn = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        val isMetered = !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)

        val primaryType = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
            isVpn -> NetworkType.VPN
            else -> NetworkType.UNKNOWN
        }

        val wifiInfo = if (primaryType == NetworkType.WIFI) fetchWifiDetails(capabilities) else null
        val cellularInfo = if (primaryType == NetworkType.CELLULAR) fetchCellularDetails() else null

        return NetworkFullState(
            isConnected = isConnected,
            primaryType = primaryType,
            isMetered = isMetered,
            isVpnActive = isVpn,
            wifiDetails = wifiInfo,
            cellularDetails = cellularInfo
        )
    }

    override fun observeStateChanges(): Flow<Unit> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { trySend(Unit) }
            override fun onLost(network: Network) { trySend(Unit) }
            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) { trySend(Unit) }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectiviManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectiviManager.unregisterNetworkCallback(callback)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchWifiDetails(capabilities: NetworkCapabilities): WifiDetails {
        val wifiInfo: WifiInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            capabilities.transportInfo as? WifiInfo
        } else {
            @Suppress("DEPRECATION")
            wifiManager.connectionInfo
        }

        val rawSsid = wifiInfo?.ssid ?: "<unknown>"
        val cleanSsid = if (rawSsid.startsWith("\"") && rawSsid.endsWith("\"")) {
            rawSsid.substring(1, rawSsid.length - 1)
        } else rawSsid

        return WifiDetails(
            ssid = if (cleanSsid == "<unknown ssid>") "Permission / Location Required" else cleanSsid,
            rssi = wifiInfo?.rssi ?: 0,
            linkSpeedMbps = wifiInfo?.linkSpeed ?: 0,
            frequencyMhz = wifiInfo?.frequency ?: 0
        )
    }

    @SuppressLint("MissingPermission")
    private fun fetchCellularDetails(): CellularDetails {
        val operatorName = telephonyManager.networkOperatorName.ifEmpty { "Unknown" }
        val isRoaming = telephonyManager.isNetworkRoaming

        val gen = try {
            when (telephonyManager.dataNetworkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE -> "2G"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                TelephonyManager.NETWORK_TYPE_LTE -> "4G (LTE)"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                else -> "Unknown"
            }
        } catch (e: Exception) {
            "Permission Required"
        }

        val simState = when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_READY -> "Ready"
            TelephonyManager.SIM_STATE_ABSENT -> "Absent"
            else -> "Not Ready"
        }

        return CellularDetails(
            operatorName = operatorName,
            networkType = gen,
            simState = simState,
            isRoaming = isRoaming
        )
    }
}
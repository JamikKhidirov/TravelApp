package com.example.network_info.data

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
import kotlinx.coroutines.flow.conflate
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
            isVpn -> NetworkType.VPN
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
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
    }.conflate()

    private fun fetchWifiDetails(capabilities: NetworkCapabilities): WifiDetails {
        return try {
            val wifiInfo: WifiInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                capabilities.transportInfo as? WifiInfo
            } else {
                @Suppress("DEPRECATION")
                wifiManager.connectionInfo
            }

            val rawSsid = wifiInfo?.ssid
            // Используем removeSurrounding для безопасного удаления кавычек
            val cleanSsid = rawSsid?.removeSurrounding("\"") ?: "<unknown>"

            WifiDetails(
                ssid = if (cleanSsid == "<unknown ssid>" || cleanSsid == "<unknown>") "Permission / Location Required" else cleanSsid,
                rssi = wifiInfo?.rssi ?: 0,
                linkSpeedMbps = wifiInfo?.linkSpeed ?: 0,
                frequencyMhz = wifiInfo?.frequency ?: 0
            )
        } catch (e: SecurityException) {
            // Если нет прав (ACCESS_FINE_LOCATION / ACCESS_WIFI_STATE), не крашимся, а возвращаем заглушку
            WifiDetails(
                ssid = "Permission Denied",
                rssi = 0,
                linkSpeedMbps = 0,
                frequencyMhz = 0
            )
        }
    }

    // Убираем @SuppressLint, заменяем на безопасный try-catch
    private fun fetchCellularDetails(): CellularDetails {
        return try {
            val operatorName = telephonyManager.networkOperatorName.ifEmpty { "Unknown" }
            val isRoaming = telephonyManager.isNetworkRoaming

            val gen = when (telephonyManager.dataNetworkType) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_1xRTT -> "2G"

                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_HSPAP,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_EVDO_B -> "3G"

                TelephonyManager.NETWORK_TYPE_LTE -> "4G (LTE)"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                else -> "Unknown"
            }

            val simState = when (telephonyManager.simState) {
                TelephonyManager.SIM_STATE_READY -> "Ready"
                TelephonyManager.SIM_STATE_ABSENT -> "Absent"
                TelephonyManager.SIM_STATE_PIN_REQUIRED -> "PIN Required"
                TelephonyManager.SIM_STATE_PUK_REQUIRED -> "PUK Required"
                else -> "Not Ready"
            }

            CellularDetails(
                operatorName = operatorName,
                networkType = gen,
                simState = simState,
                isRoaming = isRoaming
            )
        } catch (e: SecurityException) {
            // Если нет прав (READ_PHONE_STATE), не крашимся
            CellularDetails(
                operatorName = "Permission Denied",
                networkType = "Unknown",
                simState = "Unknown",
                isRoaming = false
            )
        } catch (e: Exception) {
            // Ловим любые другие неожиданности (например, если модем недоступен)
            CellularDetails(
                operatorName = "Error",
                networkType = "Unknown",
                simState = "Unknown",
                isRoaming = false
            )
        }
    }
}
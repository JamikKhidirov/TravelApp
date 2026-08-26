package com.example.network_info.domain.repository

import com.example.network_info.data.state.NetworkFullState
import kotlinx.coroutines.flow.Flow


interface NetworkRepository {

    fun getNetworkState(): NetworkFullState
    fun observeNetworkState(): Flow<NetworkFullState>

}
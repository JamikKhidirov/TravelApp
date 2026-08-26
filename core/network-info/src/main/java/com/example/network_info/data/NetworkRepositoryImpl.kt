package com.example.network_info.data

import com.example.network_info.data.state.NetworkFullState
import com.example.network_info.domain.repository.NetworkDataSource
import com.example.network_info.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class NetworkRepositoryImpl @Inject constructor(
    private val dataSource: NetworkDataSource
): NetworkRepository {

    override fun getNetworkState(): NetworkFullState {
        return dataSource.getCurrentState()
    }

    override fun observeNetworkState(): Flow<NetworkFullState> {
        return dataSource.observeStateChanges().map {
            dataSource.getCurrentState()
        }
    }
}
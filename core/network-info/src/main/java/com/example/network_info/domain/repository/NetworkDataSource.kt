package com.example.network_info.domain.repository

import com.example.network_info.data.state.NetworkFullState
import kotlinx.coroutines.flow.Flow


interface NetworkDataSource {
    fun getCurrentState(): NetworkFullState
    fun observeStateChanges(): Flow<Unit>
}
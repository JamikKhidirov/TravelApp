package com.example.network_info.domain.usecase

import com.example.network_info.data.state.NetworkFullState
import com.example.network_info.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ObserveNetworkStateUseCase @Inject constructor(
    private val repository: NetworkRepository
) {
    operator fun invoke(): Flow<NetworkFullState> = repository.observeNetworkState()
}
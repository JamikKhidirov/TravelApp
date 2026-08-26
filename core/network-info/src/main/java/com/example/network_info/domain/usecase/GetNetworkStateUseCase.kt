package com.example.network_info.domain.usecase

import com.example.network_info.data.state.NetworkFullState
import com.example.network_info.domain.repository.NetworkRepository
import javax.inject.Inject


class GetNetworkStateUseCase @Inject constructor(
    private val repository: NetworkRepository
) {
    operator fun invoke(): NetworkFullState = repository.getNetworkState()
 }
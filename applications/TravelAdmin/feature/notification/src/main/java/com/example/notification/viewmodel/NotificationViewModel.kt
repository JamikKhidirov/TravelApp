package com.example.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notification.action.NotificationAction
import com.example.notification.state.NotificationUiState
import com.example.pushing.domain.FcmPushResult
import com.example.pushing.domain.SendPushUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val sendPushUseCase: SendPushUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: NotificationAction) {
        when (action) {
            is NotificationAction.OnServerKeyChange -> {
                _uiState.update { it.copy(serverKey = action.key) }
            }
            is NotificationAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
            }
            is NotificationAction.OnBodyChange -> {
                _uiState.update { it.copy(body = action.body) }
            }
            is NotificationAction.OnTokenChange -> {
                _uiState.update { it.copy(token = action.token) }
            }
            is NotificationAction.OnSendToAllChange -> {
                _uiState.update { it.copy(sendToAll = action.sendToAll) }
            }
            NotificationAction.ClearResult -> {
                _uiState.update { it.copy(resultMessage = null, isSuccess = null) }
            }
            NotificationAction.Send -> send()
        }
    }

    private fun send() {
        val state = _uiState.value
        if (state.serverKey.isBlank()) {
            _uiState.update { it.copy(resultMessage = "Введите Server Key", isSuccess = false) }
            return
        }
        if (state.title.isBlank() || state.body.isBlank()) {
            _uiState.update { it.copy(resultMessage = "Заполните заголовок и текст", isSuccess = false) }
            return
        }
        if (!state.sendToAll && state.token.isBlank()) {
            _uiState.update { it.copy(resultMessage = "Введите токен устройства", isSuccess = false) }
            return
        }

        _uiState.update { it.copy(isSending = true, resultMessage = null, isSuccess = null) }

        viewModelScope.launch {
            val result = sendPushUseCase(
                serverKey = state.serverKey,
                title = state.title,
                body = state.body,
                token = if (state.sendToAll) null else state.token
            )
            _uiState.update {
                it.copy(
                    isSending = false,
                    resultMessage = when (result) {
                        is FcmPushResult.Success -> "Уведомление отправлено!"
                        is FcmPushResult.Error -> "Ошибка: ${result.message}"
                    },
                    isSuccess = result is FcmPushResult.Success
                )
            }
        }
    }
}

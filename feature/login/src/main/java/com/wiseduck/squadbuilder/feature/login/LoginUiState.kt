package com.wiseduck.squadbuilder.feature.login

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val eventSink: (LoginUiEvent) -> Unit
) : CircuitUiState

sealed interface LoginUiEvent : CircuitUiEvent {
    data object OnKakaoLoginButtonClick : LoginUiEvent
    data object OnCloseDialogButtonClick : LoginUiEvent
}
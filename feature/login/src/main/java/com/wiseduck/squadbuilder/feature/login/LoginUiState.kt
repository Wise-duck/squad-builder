package com.wiseduck.squadbuilder.feature.login

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val eventSink: (LoginUiEvent) -> Unit,
    val sideEffect: LoginSideEffects? = null,
) : CircuitUiState

sealed interface LoginSideEffects {
    data object LaunchKakaoLogin : LoginSideEffects
}

sealed interface LoginUiEvent : CircuitUiEvent {
    data object OnKakaoLoginButtonClick : LoginUiEvent

    data object OnCloseDialogButtonClick : LoginUiEvent

    data class OnLoginSuccess(
        val accessToken: String,
    ) : LoginUiEvent

    data class OnLoginFailure(
        val errorMessage: String,
    ) : LoginUiEvent

    data object InitSideEffect : LoginUiEvent
}

package com.wiseduck.squadbuilder.feature.login

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.wiseduck.squadbuilder.core.common.utils.UiText

data class LoginUiState(
    val isLoading: Boolean = false,
    val eventSink: (LoginUiEvent) -> Unit,
    val sideEffect: LoginSideEffect? = null,
) : CircuitUiState

@Immutable
sealed interface LoginSideEffect {
    data object LaunchKakaoLogin : LoginSideEffect

    data class ShowToast(
        val messge: UiText,
    ) : LoginSideEffect
}

sealed interface LoginUiEvent : CircuitUiEvent {
    data object InitSideEffect : LoginUiEvent

    data object OnKakaoLoginButtonClick : LoginUiEvent

    data class OnLoginSuccess(
        val accessToken: String,
    ) : LoginUiEvent

    data class OnLoginFailure(
        val message: UiText,
    ) : LoginUiEvent

    data object OnLoginRequired : LoginUiEvent
}

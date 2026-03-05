package com.wiseduck.squadbuilder.feature.splash

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val isLoading: Boolean = false,
    val isUpdateDialogVisible: Boolean = false,
    val sideEffect: SplashSideEffect? = null,
    val eventSink: (SplashUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface SplashSideEffect {
    data object OnUpdateClick : SplashSideEffect
}

sealed interface SplashUiEvent : CircuitUiEvent {
    data object InitSideEffect : SplashUiEvent

    data object OnCloseDialogButtonClick : SplashUiEvent

    data object OnUpdateButtonClick : SplashUiEvent
}

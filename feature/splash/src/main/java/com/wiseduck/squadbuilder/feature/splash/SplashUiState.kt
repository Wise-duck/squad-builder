package com.wiseduck.squadbuilder.feature.splash

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val isLoading: Boolean = false,
    val isUpdateDialogVisible: Boolean = false,
    val eventSink: (SplashUiEvent) -> Unit
) : CircuitUiState

sealed interface SplashUiEvent : CircuitUiEvent {
    data object OnCloseDialogButtonClick : SplashUiEvent
    data object OnUpdateButtonClick : SplashUiEvent
}
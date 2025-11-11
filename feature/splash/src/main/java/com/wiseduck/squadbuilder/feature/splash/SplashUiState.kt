package com.wiseduck.squadbuilder.feature.splash

import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val isLoading: Boolean = false
) : CircuitUiState

sealed interface SplashUiEvent {

}
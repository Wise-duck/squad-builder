package com.wiseduck.squadbuilder.feature.home

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class HomeUiState(
    val isLoading: Boolean = true,
    val eventSink: (HomeUiEvent) -> Unit
) : CircuitUiState

sealed interface HomeUiEvent: CircuitUiEvent {
    data class OnMyTeamDetailClick(
        val teamId: String
    ) : HomeUiEvent
}
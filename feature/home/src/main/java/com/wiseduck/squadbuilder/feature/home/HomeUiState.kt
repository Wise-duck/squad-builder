package com.wiseduck.squadbuilder.feature.home

import com.slack.circuit.runtime.CircuitUiEvent

data class HomeUiState(
    val isLoading: Boolean = true,
    val eventSink: (HomeUiEvent) -> Unit
) : CircuitUiEvent

sealed interface HomeUiEvent: CircuitUiEvent {
    data class OnMyTeamDetailClick(
        val teamId: String
    ) : HomeUiEvent
}
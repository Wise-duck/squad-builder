package com.wiseduck.squadbuilder.feature.detail.team

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class MyTeamDetailUiState(
    val eventSink: (MyTeamDetailUiEvent) -> Unit
) : CircuitUiState

sealed interface MyTeamDetailUiEvent : CircuitUiEvent {
    data object OnManagePlayersClick : MyTeamDetailUiEvent
    data object OnManageFormationClick : MyTeamDetailUiEvent
}
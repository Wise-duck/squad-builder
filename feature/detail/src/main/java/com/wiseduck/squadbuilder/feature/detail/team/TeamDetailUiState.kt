package com.wiseduck.squadbuilder.feature.detail.team

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.wiseduck.squadbuilder.core.model.TeamModel

data class TeamDetailUiState(
    val isLoading: Boolean = false,
    val team: TeamModel? = null,
    val eventSink: (TeamDetailEvent) -> Unit,
) : CircuitUiState

sealed interface TeamDetailEvent : CircuitUiEvent {
    data object OnManagePlayersClick : TeamDetailEvent

    data object OnManageFormationClick : TeamDetailEvent

    data object OnBackButtonClick : TeamDetailEvent
}

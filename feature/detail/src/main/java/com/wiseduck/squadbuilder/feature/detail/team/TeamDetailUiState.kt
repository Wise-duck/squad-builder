package com.wiseduck.squadbuilder.feature.detail.team

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.wiseduck.squadbuilder.core.model.Team

data class TeamDetailUiState(
    val isLoading: Boolean = false,
    val admobBannerId: String,
    val team: Team? = null,
    val eventSink: (TeamDetailEvent) -> Unit,
) : CircuitUiState

sealed interface TeamDetailEvent : CircuitUiEvent {
    data object OnManagePlayersClick : TeamDetailEvent

    data object OnManageFormationClick : TeamDetailEvent

    data object OnBackButtonClick : TeamDetailEvent
}

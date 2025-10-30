package com.wiseduck.squadbuilder.feature.home

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.core.model.TeamModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val isLoading: Boolean = false,
    val teams: ImmutableList<TeamModel> = persistentListOf(),
    val eventSink: (HomeUiEvent) -> Unit
) : CircuitUiState

sealed interface HomeUiEvent: CircuitUiEvent {
    data class OnTeamCardClick(
        val teamId: Int
    ) : HomeUiEvent
    data class OnTeamDeleteButtonClick(
        val teamId: Int
    ) : HomeUiEvent
    data class OnTabSelect(
        val screen: Screen
    ) : HomeUiEvent
}
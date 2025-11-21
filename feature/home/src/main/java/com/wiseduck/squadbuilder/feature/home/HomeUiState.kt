package com.wiseduck.squadbuilder.feature.home

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.core.model.TeamModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val currentSortOption: TeamSortOption = TeamSortOption.LATEST,
    val errorMessage: String? = null,
    val teams: ImmutableList<TeamModel> = persistentListOf(),
    val eventSink: (HomeUiEvent) -> Unit
) : CircuitUiState

enum class TeamSortOption {
    LATEST,
    NAME
}

sealed interface HomeUiEvent: CircuitUiEvent {
    data object OnRefresh: HomeUiEvent
    data class OnTeamCreateButtonClick(
        val teamName: String
    ) : HomeUiEvent
    data class OnSortOptionSelect(
        val sortOption: TeamSortOption
    ) : HomeUiEvent
    data class OnTeamCardClick(
        val teamId: Int,
        val teamName: String
    ) : HomeUiEvent
    data class OnTeamDeleteButtonClick(
        val teamId: Int
    ) : HomeUiEvent
    data class OnTabSelect(
        val screen: Screen
    ) : HomeUiEvent
    data object OnDialogCloseButtonClick : HomeUiEvent
}
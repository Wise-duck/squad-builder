package com.wiseduck.squadbuilder.feature.home

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.core.common.utils.UiText
import com.wiseduck.squadbuilder.core.model.TeamModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoggedIn: Boolean = false,
    val adUnitId: String = "",
    val currentSortOption: TeamSortOption = TeamSortOption.LATEST,
    val teams: ImmutableList<TeamModel> = persistentListOf(),
    val teamToDelete: TeamModel? = null,
    val sideEffect: HomeSideEffect? = null,
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface HomeSideEffect {
    data class ShowToast(
        val message: UiText,
    ) : HomeSideEffect
}

enum class TeamSortOption {
    LATEST,
    NAME,
}

sealed interface HomeUiEvent : CircuitUiEvent {
    data object InitSideEffect : HomeUiEvent

    data object OnRefresh : HomeUiEvent

    data class OnTeamCreateButtonClick(
        val teamName: String,
    ) : HomeUiEvent

    data class OnSortOptionSelect(
        val sortOption: TeamSortOption,
    ) : HomeUiEvent

    data class OnTeamCardClick(
        val teamId: Int,
        val teamName: String,
    ) : HomeUiEvent

    data class OnTeamDeleteClick(
        val team: TeamModel,
    ) : HomeUiEvent

    data class OnTeamDeleteConfirm(
        val teamId: Int,
    ) : HomeUiEvent

    data object OnDismissTeamDeleteDialog : HomeUiEvent

    data class OnTabSelect(
        val screen: Screen,
    ) : HomeUiEvent
}

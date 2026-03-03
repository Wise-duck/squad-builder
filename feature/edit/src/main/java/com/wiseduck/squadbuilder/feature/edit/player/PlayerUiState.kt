package com.wiseduck.squadbuilder.feature.edit.player

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlayerUiState(
    val isLoading: Boolean = false,
    val admobBannerId: String,
    val teamName: String,
    val errorMessage: String? = null,
    val currentEditingPlayerId: Int? = null,
    val isShowPlayerCreationSection: Boolean = false,
    val players: ImmutableList<TeamPlayerModel> = persistentListOf(),
    val eventSink: (PlayerUiEvent) -> Unit,
) : CircuitUiState

sealed interface PlayerUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : PlayerUiEvent

    data object OnTeamPlayerCreationButtonClick : PlayerUiEvent

    data object OnTeamPlayerCreationCancelButtonClick : PlayerUiEvent

    data class OnTeamPlayerCreationConfirmButtonClick(
        val name: String,
        val position: String,
        val backNumber: Int,
    ) : PlayerUiEvent

    data class OnTeamPlayerEditButtonClick(
        val playerId: Int,
    ) : PlayerUiEvent

    data class OnPlayerUpdateConfirm(
        val playerId: Int,
        val name: String,
        val position: String,
        val backNumber: Int,
    ) : PlayerUiEvent

    data object OnPlayerUpdateCancel : PlayerUiEvent

    data class OnTeamPlayerDeleteButtonClick(
        val playerId: Int,
    ) : PlayerUiEvent

    data object OnDialogCloseButtonClick : PlayerUiEvent

    data class OnTabSelect(
        val screen: Screen,
    ) : PlayerUiEvent
}

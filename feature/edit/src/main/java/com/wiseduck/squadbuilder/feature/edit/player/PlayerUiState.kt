package com.wiseduck.squadbuilder.feature.edit.player

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.core.common.utils.UiText
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlayerUiState(
    val isLoading: Boolean = false,
    val admobBannerId: String,
    val teamName: String,
    val currentEditingPlayerId: Int? = null,
    val isShowPlayerCreationSection: Boolean = false,
    val players: ImmutableList<TeamPlayerModel> = persistentListOf(),
    val sideEffect: PlayerSideEffect? = null,
    val eventSink: (PlayerUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface PlayerSideEffect {
    data class ShowToast(
        val message: UiText,
    ) : PlayerSideEffect
}

sealed interface PlayerUiEvent : CircuitUiEvent {
    data object InitSideEffect : PlayerUiEvent

    data object OnBackClick : PlayerUiEvent

    data object OnPlayerCreationClick : PlayerUiEvent

    data object OnPlayerCreationCancelClick : PlayerUiEvent

    data class OnPlayerCreationConfirmClick(
        val name: String,
        val position: String,
        val backNumber: Int,
    ) : PlayerUiEvent

    data class OnPlayerEditClick(
        val playerId: Int,
    ) : PlayerUiEvent

    data class OnPlayerUpdateConfirm(
        val playerId: Int,
        val name: String,
        val position: String,
        val backNumber: Int,
    ) : PlayerUiEvent

    data object OnPlayerUpdateCancel : PlayerUiEvent

    data class OnPlayerDeleteClick(
        val playerId: Int,
    ) : PlayerUiEvent

    data class OnTabSelect(
        val screen: Screen,
    ) : PlayerUiEvent
}

package com.wiseduck.squadbuilder.feature.edit.formation

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.Placement
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel

data class PlayerAssignmentState(
    val isDialogVisible: Boolean = false,
    val slotId: Int? = null
)

data class DeleteConfirmationState(
    val isDialogVisible: Boolean = false,
    val formationIdToDelete: Int? = null
)
data class FormationUiState(
    val teamId: Int = 0,
    val teamName: String = "",
    val eventSink: (FormationUiEvent) -> Unit,
    val formationList: List<FormationListItemModel> = emptyList(),
    val isListModalVisible: Boolean = false,
    val players: List<Placement> = emptyList(),
    val selectedSlotId: Int? = null,
    val draggedPlayerInitialPosition: Placement? = null,
    val isResetConfirmDialogVisible: Boolean = false,
    val currentFormationId: Int? = null,
    val currentFormationName: String = "",
    val isSaveDialogVisible: Boolean = false,
    val toastMessage: String? = null,
    val availablePlayers: List<TeamPlayerModel> = emptyList(),
    val playerAssignmentState: PlayerAssignmentState = PlayerAssignmentState(),
    val deleteConfirmationState: DeleteConfirmationState = DeleteConfirmationState()
) : CircuitUiState

sealed interface FormationUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : FormationUiEvent
    data object OnFormationResetClick : FormationUiEvent
    data object OnFormationListClick : FormationUiEvent
    data object OnFormationSaveClick : FormationUiEvent
    data object OnDismissListModal : FormationUiEvent
    data class OnPlayerClick(val slotId: Int) : FormationUiEvent
    data object OnDismissPlayerInfoDialog : FormationUiEvent

    data class OnPlayerDragStart(val slotId: Int) : FormationUiEvent
    data class OnPlayerDrag(val slotId: Int, val deltaCoordX: Float, val deltaCoordY: Float) : FormationUiEvent
    data class OnPlayerDragEnd(val slotId: Int, val relativeChipWidth: Float, val relativeChipHeight: Float) : FormationUiEvent

    data object OnConfirmReset : FormationUiEvent
    data object OnDismissResetDialog : FormationUiEvent
    data class OnFormationCardClick(val formationId: Int) : FormationUiEvent
    data class OnFormationNameChange(val name: String) : FormationUiEvent

    data object OnSaveDialogConfirm : FormationUiEvent
    data object OnSaveDialogDismiss : FormationUiEvent
    data object OnToastShown : FormationUiEvent

    // --- 선수 배정 관련 이벤트 ---
    data class OnAssignPlayer(val playerIdToAssign: Int) : FormationUiEvent
    data object OnUnassignPlayer : FormationUiEvent
    data object OnDismissPlayerAssignmentDialog : FormationUiEvent

    data object OnModifyPlayerClick : FormationUiEvent

    data class OnDeleteFormationClick(val formationId: Int) : FormationUiEvent
    data object OnDeleteFormationConfirm : FormationUiEvent
    data object OnDismissDeleteDialog : FormationUiEvent
}

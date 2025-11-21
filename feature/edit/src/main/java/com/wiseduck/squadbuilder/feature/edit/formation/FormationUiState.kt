package com.wiseduck.squadbuilder.feature.edit.formation

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.PlacementModel
import com.wiseduck.squadbuilder.core.model.PlayerQuarterStatusModel
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
    val currentQuarter: Int = 1,
    val formationList: List<FormationListItemModel> = emptyList(),
    val isListModalVisible: Boolean = false,
    val players: List<PlacementModel> = emptyList(),
    val selectedSlotId: Int? = null,
    val draggedPlayerInitialPosition: PlacementModel? = null,
    val isResetConfirmDialogVisible: Boolean = false,
    val isQuarterSelectionDialogVisible: Boolean = false,
    
    val isPlayerQuarterStatusVisible: Boolean = false,
    val playerQuarterStatus: List<PlayerQuarterStatusModel> = emptyList(),

    val currentFormationId: Int? = null,
    val currentFormationName: String = "",
    val isSaveDialogVisible: Boolean = false,
    val toastMessage: String? = null,
    val availablePlayers: List<TeamPlayerModel> = emptyList(),
    val playerAssignmentState: PlayerAssignmentState = PlayerAssignmentState(),
    val deleteConfirmationState: DeleteConfirmationState = DeleteConfirmationState(),
    val isCapturing: Boolean = false,
    val totalQuartersToCapture: Int = 0,
    val sideEffect: FormationSideEffect? = null
) : CircuitUiState

sealed interface FormationSideEffect {
    data class CaptureFormation(
        val quarter: Int,
        val onCaptureUri: (Int, Uri?) -> Unit
    ) : FormationSideEffect

    data class ShareMultipleImages(
        val imageUris: List<Uri>
    ) : FormationSideEffect
}

sealed interface FormationUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : FormationUiEvent
    data object OnFormationResetClick : FormationUiEvent
    data object OnFormationListClick : FormationUiEvent
    data object OnFormationShareClick: FormationUiEvent
    data class OnQuarterChange(
        val quarter: Int
    ) : FormationUiEvent
    data object OnFormationSaveClick : FormationUiEvent
    data object OnDismissListModal : FormationUiEvent
    data class OnPlayerClick(val slotId: Int) : FormationUiEvent
    data object OnDismissPlayerInfoDialog : FormationUiEvent
    data class OnSelectQuartersToShare(
        val quarters: Set<Int>
    ): FormationUiEvent
    data object OnDismissQuarterSelectionDialog: FormationUiEvent

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

    data object OnPlayerQuarterStatusClick : FormationUiEvent

    // --- 선수 배정 관련 이벤트 ---
    data class OnAssignPlayer(val playerIdToAssign: Int) : FormationUiEvent
    data object OnUnassignPlayer : FormationUiEvent
    data object OnDismissPlayerAssignmentDialog : FormationUiEvent
    data object OnModifyPlayerClick : FormationUiEvent
    data class OnDeleteFormationClick(val formationId: Int) : FormationUiEvent
    data object OnDeleteFormationConfirm : FormationUiEvent
    data object OnDismissDeleteDialog : FormationUiEvent
}

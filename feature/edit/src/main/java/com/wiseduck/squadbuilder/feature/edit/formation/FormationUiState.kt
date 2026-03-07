package com.wiseduck.squadbuilder.feature.edit.formation

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.wiseduck.squadbuilder.core.common.utils.UiText
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.PlacementModel
import com.wiseduck.squadbuilder.core.model.PlayerQuarterStatusModel
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class PlayerAssignmentState(
    val isDialogVisible: Boolean = false,
    val slotId: Int? = null,
)

data class DeleteConfirmationState(
    val isDialogVisible: Boolean = false,
    val formationIdToDelete: Int? = null,
)

data class FormationUiState(
    val teamId: Int = 0,
    val teamName: String = "",
    val isLoading: Boolean = false,
    val currentQuarter: Int = 1,
    val allReferees: PersistentMap<Int, String> = persistentMapOf(),
    val formationList: ImmutableList<FormationListItemModel> = persistentListOf(),
    val isListModalVisible: Boolean = false,
    val players: ImmutableList<PlacementModel> = persistentListOf(),
    val selectedSlotId: Int? = null,
    val draggedPlayerInitialPosition: PlacementModel? = null,
    val isResetConfirmDialogVisible: Boolean = false,
    val isQuarterSelectionDialogVisible: Boolean = false,
    val isPlayerQuarterStatusVisible: Boolean = false,
    val playerQuarterStatus: ImmutableList<PlayerQuarterStatusModel> = persistentListOf(),
    val currentFormationId: Int? = null,
    val currentFormationName: String = "",
    val isSaveDialogVisible: Boolean = false,
    val availablePlayers: ImmutableList<TeamPlayerModel> = persistentListOf(),
    val playerAssignmentState: PlayerAssignmentState = PlayerAssignmentState(),
    val deleteConfirmationState: DeleteConfirmationState = DeleteConfirmationState(),
    val isCapturing: Boolean = false,
    val totalQuartersToCapture: Int = 0,
    val sideEffect: FormationSideEffect? = null,
    val eventSink: (FormationUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface FormationSideEffect {
    data class CaptureFormation(
        val quarter: Int,
        val onCaptureUri: (Int, Uri?) -> Unit,
    ) : FormationSideEffect

    data class ShareMultipleImages(
        val imageUris: ImmutableList<Uri>,
    ) : FormationSideEffect

    data class ShowToast(
        val message: UiText,
    ) : FormationSideEffect
}

sealed interface FormationUiEvent : CircuitUiEvent {
    data object InitSideEffect : FormationUiEvent

    data object OnBackClick : FormationUiEvent
    data object OnFormationListClick : FormationUiEvent
    data object OnFormationResetClick : FormationUiEvent
    data object OnFormationSaveClick : FormationUiEvent
    data object OnFormationShareClick : FormationUiEvent
    data object OnPlayerQuarterStatusClick : FormationUiEvent

    data class OnSelectQuartersToShare(
        val quarters: Set<Int>,
    ) : FormationUiEvent
    data class OnQuarterChange(
        val quarter: Int,
    ) : FormationUiEvent
    data class OnRefereeNameChange(
        val quarter: Int,
        val refereeName: String,
    ) : FormationUiEvent
    ) : FormationUiEvent

    data class OnPlayerClick(val slotId: Int) : FormationUiEvent
    data class OnPlayerDragStart(val slotId: Int) : FormationUiEvent
    data class OnPlayerDrag(val slotId: Int, val deltaCoordX: Float, val deltaCoordY: Float) : FormationUiEvent
    data class OnPlayerDragEnd(val slotId: Int, val relativeChipWidth: Float, val relativeChipHeight: Float) : FormationUiEvent

    data class OnAssignPlayer(val playerIdToAssign: Int) : FormationUiEvent
    data object OnUnassignPlayer : FormationUiEvent
    data object OnModifyPlayerClick : FormationUiEvent

    data class OnFormationCardClick(val formationId: Int) : FormationUiEvent
    data class OnFormationNameChange(val name: String) : FormationUiEvent

    data object OnFormationSaveConfirm : FormationUiEvent
    data object OnFormationResetConfirm : FormationUiEvent
    data object OnFormationDeleteConfirm : FormationUiEvent
    data class OnFormationDeleteClick(val formationId: Int) : FormationUiEvent

    data object OnDismissQuarterSelectionDialog : FormationUiEvent
    data object OnDismissFormationListModal : FormationUiEvent
    data object OnDismissFormationResetDialog : FormationUiEvent
    data object OnDismissPlayerInfoDialog : FormationUiEvent
    data object OnDismissPlayerAssignmentDialog : FormationUiEvent
    data object OnDismissFormationDeleteDialog : FormationUiEvent
    data object OnDismissFormationSaveDialog : FormationUiEvent
}

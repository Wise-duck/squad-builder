package com.wiseduck.squadbuilder.feature.edit.formation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.data.api.repository.FormationRepository
import com.wiseduck.squadbuilder.core.data.api.repository.PlayerRepository
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.FormationSaveModel
import com.wiseduck.squadbuilder.core.model.Placement
import com.wiseduck.squadbuilder.core.model.PlacementSaveModel
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers
import com.wiseduck.squadbuilder.feature.edit.formation.data.getPositionForCoordinates
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class FormationPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: FormationScreen,
    private val formationRepository: FormationRepository,
    private val playerRepository: PlayerRepository
) : Presenter<FormationUiState> {
    @Composable
    override fun present(): FormationUiState {
        val teamId = screen.teamId
        val teamName = screen.teamName

        val scope = rememberCoroutineScope()
        var formationList by remember { mutableStateOf(emptyList<FormationListItemModel>()) }
        var isListModalVisible by remember { mutableStateOf(false) }
        var isResetConfirmDialogVisible by remember { mutableStateOf(false) }

        var players by remember { mutableStateOf(emptyList<Placement>()) }
        var selectedSlotId by remember { mutableStateOf<Int?>(null) }

        var draggedPlayerInitialPosition by remember { mutableStateOf<Placement?>(null) }
        var currentFormationId by remember { mutableStateOf<Int?>(null) }
        var currentFormationName by remember { mutableStateOf("") }

        var isSaveDialogVisible by remember { mutableStateOf(false) }
        var toastMessage by remember { mutableStateOf<String?>(null) }

        var availablePlayers by remember { mutableStateOf(emptyList<TeamPlayerModel>()) }
        var playerAssignmentState by remember { mutableStateOf(PlayerAssignmentState()) }

        var deleteConfirmationState by remember { mutableStateOf(DeleteConfirmationState()) }

        var isFormationSharing by remember { mutableStateOf(false) }
        var sideEffect by remember { mutableStateOf<FormationSideEffect?>(null) }

        LaunchedEffect(Unit) {
            playerRepository.getTeamPlayers(screen.teamId)
                .onSuccess {
                    availablePlayers = it
                }
                .onFailure {
                    // TODO: 선수 목록 불러오기 실패 처리
                }

            players = createDefaultPlayers()
        }

        fun handleEvent(event: FormationUiEvent) {
            when (event) {
                is FormationUiEvent.OnFormationShareClick -> {
                    isFormationSharing = true
                }

                is FormationUiEvent.ShareFormation -> {
                    isFormationSharing = false
                    sideEffect = FormationSideEffect.ShareFormation(event.imageBitmap)
                }

                FormationUiEvent.OnBackButtonClick -> {
                    navigator.pop()
                }

                FormationUiEvent.OnFormationResetClick -> {
                    isResetConfirmDialogVisible = true
                }

                FormationUiEvent.OnConfirmReset -> {
                    players = createDefaultPlayers()
                    currentFormationId = null
                    currentFormationName = ""
                    isResetConfirmDialogVisible = false
                }

                FormationUiEvent.OnDismissResetDialog -> {
                    isResetConfirmDialogVisible = false
                }

                FormationUiEvent.OnFormationListClick -> {
                    scope.launch {
                        formationRepository.getFormationList(teamId)
                            .onSuccess { list ->
                                formationList = list
                                isListModalVisible = true
                            }
                            .onFailure {  }

                    }
                }

                is FormationUiEvent.OnFormationCardClick -> {
                    scope.launch {
                        formationRepository.getFormationDetail(event.formationId)
                            .onSuccess { formationDetail ->
                                players = formationDetail.placements
                                currentFormationId = formationDetail.formationId
                                currentFormationName = formationDetail.name
                                isListModalVisible = false
                            }
                            .onFailure {
                                toastMessage = "불러오기에 실패했습니다."
                            }
                    }
                }

                is FormationUiEvent.OnFormationNameChange -> {
                    currentFormationName = event.name
                }

                FormationUiEvent.OnFormationSaveClick -> {
                    val isAnyPlayerAssigned = players.any { it.playerId != null }
                    if (isAnyPlayerAssigned) {
                        isSaveDialogVisible = true
                    } else {
                        toastMessage = "선수를 배치해주세요."
                    }
                }

                FormationUiEvent.OnSaveDialogConfirm -> {
                    scope.launch {
                        val isUpdate = currentFormationId != null
                        val placements = players.mapNotNull { placement ->
                            placement.playerId?.let {
                                PlacementSaveModel(
                                    playerId = it,
                                    quarter = 1,
                                    coordX = (placement.coordX * 1000).toInt(),
                                    coordY = (placement.coordY * 1000).toInt()
                                )
                            }
                        }

                        val request = FormationSaveModel(
                            teamId = teamId,
                            name = currentFormationName.ifBlank { "새 포메이션" },
                            placements = placements
                        )

                        if (isUpdate) {
                            formationRepository.updateFormation(currentFormationId!!, request)
                                .onSuccess {
                                    toastMessage = "수정되었습니다."
                                }
                                .onFailure {
                                    toastMessage = "저장에 실패했습니다."
                                }
                        } else {
                            formationRepository.createFormation(request)
                                .onSuccess {
                                    toastMessage = "저장되었습니다."
                                    scope.launch {
                                        formationRepository.getFormationList(teamId)
                                            .onSuccess { newList ->
                                                formationList = newList
                                            }
                                    }
                                }
                                .onFailure {
                                    toastMessage = "저장에 실패했습니다."
                                }
                        }
                    }
                    isSaveDialogVisible = false
                }

                FormationUiEvent.OnSaveDialogDismiss -> {
                    isSaveDialogVisible = false
                }

                FormationUiEvent.OnToastShown -> {
                    toastMessage = null
                }

                FormationUiEvent.OnDismissListModal -> {
                    isListModalVisible = false
                }

                is FormationUiEvent.OnPlayerClick -> {
                    val clickedSlot = players.find { it.slotId == event.slotId }
                    if (clickedSlot != null) {
                        if (clickedSlot.playerId == null) {
                            playerAssignmentState = PlayerAssignmentState(
                                isDialogVisible = true,
                                slotId = clickedSlot.slotId
                            )
                        } else {
                            selectedSlotId = if (selectedSlotId == event.slotId) null else event.slotId
                        }
                    }
                }

                FormationUiEvent.OnDismissPlayerInfoDialog -> {
                    selectedSlotId = null
                }

                is FormationUiEvent.OnPlayerDragStart -> {
                    draggedPlayerInitialPosition = players.find { it.slotId == event.slotId }
                }

                is FormationUiEvent.OnPlayerDrag -> {
                    players = players.map { player ->
                        if (player.slotId == event.slotId) {
                            player.copy(
                                coordX = (player.coordX + event.deltaCoordX).coerceIn(0f, 1f),
                                coordY = (player.coordY + event.deltaCoordY).coerceIn(0f, 1f)
                            )
                        } else {
                            player
                        }
                    }
                }

                is FormationUiEvent.OnPlayerDragEnd -> {
                    val draggedPlayer = players.find { it.slotId == event.slotId }
                    val initialPos = draggedPlayerInitialPosition
                    if (draggedPlayer != null && initialPos != null) {
                        val overlappedPlayer = players.firstOrNull { otherPlayer ->
                            if (otherPlayer.slotId == draggedPlayer.slotId) {
                                false
                            } else {
                                val draggedLeft = draggedPlayer.coordX - event.relativeChipWidth / 2
                                val draggedRight = draggedPlayer.coordX + event.relativeChipWidth / 2
                                val draggedTop = draggedPlayer.coordY - event.relativeChipHeight / 2
                                val draggedBottom = draggedPlayer.coordY + event.relativeChipHeight / 2

                                val otherLeft = otherPlayer.coordX - event.relativeChipWidth / 2
                                val otherRight = otherPlayer.coordX + event.relativeChipWidth / 2
                                val otherTop = otherPlayer.coordY - event.relativeChipHeight / 2
                                val otherBottom = otherPlayer.coordY + event.relativeChipHeight / 2

                                draggedLeft < otherRight && draggedRight > otherLeft &&
                                        draggedTop < otherBottom && draggedBottom > otherTop
                            }
                        }
                        if (overlappedPlayer != null) {
                            players = players.map { p ->
                                when (p.slotId) {
                                    overlappedPlayer.slotId -> {
                                        val newPosition = getPositionForCoordinates(initialPos.coordX, initialPos.coordY)
                                        p.copy(
                                            coordX = initialPos.coordX,
                                            coordY = initialPos.coordY,
                                            playerPosition = newPosition
                                        )
                                    }
                                    draggedPlayer.slotId -> {
                                        val newPosition = getPositionForCoordinates(overlappedPlayer.coordX, overlappedPlayer.coordY)
                                        p.copy(
                                            coordX = overlappedPlayer.coordX,
                                            coordY = overlappedPlayer.coordY,
                                            playerPosition = newPosition
                                        )
                                    }
                                    else -> p
                                }
                            }
                        } else {
                            players = players.map { p ->
                                if (p.slotId == draggedPlayer.slotId) {
                                    val newPosition = getPositionForCoordinates(p.coordX, p.coordY)
                                    p.copy(playerPosition = newPosition)
                                } else {
                                    p
                                }
                            }
                        }
                    }
                    draggedPlayerInitialPosition = null
                }

                is FormationUiEvent.OnAssignPlayer -> {
                    val targetSlotId = playerAssignmentState.slotId
                    val playerToAssign = availablePlayers.find { it.id == event.playerIdToAssign }

                    if (targetSlotId != null && playerToAssign != null) {
                        players = players.map {
                            if (it.slotId == targetSlotId) {
                                it.copy(
                                    playerId = playerToAssign.id,
                                    playerName = playerToAssign.name,
                                    playerBackNumber = playerToAssign.backNumber.toString()
                                )
                            } else {
                                it
                            }
                        }
                    }
                    playerAssignmentState = PlayerAssignmentState(isDialogVisible = false, slotId = null)
                }

                FormationUiEvent.OnUnassignPlayer -> {
                    val targetSlotId = selectedSlotId
                    if (targetSlotId != null) {
                        players = players.map {
                            if (it.slotId == targetSlotId) {
                                it.copy(
                                    playerId = null,
                                    playerName = "Player",
                                    playerBackNumber = "+"
                                )
                            } else {
                                it
                            }
                        }
                    }
                    selectedSlotId = null
                }

                FormationUiEvent.OnDismissPlayerAssignmentDialog -> {
                    playerAssignmentState = PlayerAssignmentState(isDialogVisible = false, slotId = null)
                }

                is FormationUiEvent.OnDeleteFormationClick -> {
                    deleteConfirmationState = DeleteConfirmationState(
                        isDialogVisible = true,
                        formationIdToDelete = event.formationId
                    )
                }

                FormationUiEvent.OnDeleteFormationConfirm -> {
                    val formationId = deleteConfirmationState.formationIdToDelete
                    if (formationId != null) {
                        scope.launch {
                            formationRepository.deleteFormation(formationId)
                                .onSuccess {
                                    toastMessage = "삭제되었습니다."
                                    formationList = formationList.filter { it.formationId != formationId }
                                    if (currentFormationId == formationId) {
                                        currentFormationId = null
                                        currentFormationName = ""
                                    }
                                }
                                .onFailure {
                                    toastMessage = "삭제에 실패했습니다."
                                }
                        }
                    }
                    deleteConfirmationState = DeleteConfirmationState()
                }

                FormationUiEvent.OnDismissDeleteDialog -> {
                    deleteConfirmationState = DeleteConfirmationState()
                }

                FormationUiEvent.OnModifyPlayerClick -> {
                    val targetSlotId = selectedSlotId
                    if (targetSlotId != null) {
                        playerAssignmentState = PlayerAssignmentState(
                            isDialogVisible = true,
                            slotId = targetSlotId
                        )
                    }
                    selectedSlotId = null
                }
            }
        }

        val assignedPlayerIds = players.mapNotNull { it.playerId }.toSet()
        val filteredAvailablePlayers = availablePlayers.filter { it.id !in assignedPlayerIds }

        return FormationUiState(
            teamId = teamId,
            teamName = teamName,
            formationList = formationList,
            isListModalVisible = isListModalVisible,
            players = players,
            selectedSlotId = selectedSlotId,
            draggedPlayerInitialPosition = draggedPlayerInitialPosition,
            isResetConfirmDialogVisible = isResetConfirmDialogVisible,
            currentFormationId = currentFormationId,
            currentFormationName = currentFormationName,
            isSaveDialogVisible = isSaveDialogVisible,
            toastMessage = toastMessage,
            availablePlayers = filteredAvailablePlayers,
            playerAssignmentState = playerAssignmentState,
            deleteConfirmationState = deleteConfirmationState,
            isFormationSharing = isFormationSharing,
            sideEffect = sideEffect,
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: FormationScreen,
            navigator: Navigator
        ): FormationPresenter
    }
}
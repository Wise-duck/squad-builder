package com.wiseduck.squadbuilder.feature.edit.formation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.data.api.repository.FormationRepository
import com.wiseduck.squadbuilder.core.data.api.repository.PlayerRepository
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.FormationSaveModel
import com.wiseduck.squadbuilder.core.model.PlacementModel
import com.wiseduck.squadbuilder.core.model.PlacementSaveModel
import com.wiseduck.squadbuilder.core.model.PlayerQuarterStatusModel
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.feature.edit.R
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

        var currentQuarter by remember { mutableIntStateOf(1) }
        var allPlacements by remember { mutableStateOf(mapOf<Int, List<PlacementModel>>()) }

        val scope = rememberCoroutineScope()
        var formationList by remember { mutableStateOf(emptyList<FormationListItemModel>()) }
        var isListModalVisible by remember { mutableStateOf(false) }
        var isResetConfirmDialogVisible by remember { mutableStateOf(false) }

        var players by remember { mutableStateOf(emptyList<PlacementModel>()) }
        var selectedSlotId by remember { mutableStateOf<Int?>(null) }

        var draggedPlayerInitialPosition by remember { mutableStateOf<PlacementModel?>(null) }
        var currentFormationId by remember { mutableStateOf<Int?>(null) }
        var currentFormationName by remember { mutableStateOf("") }

        var isQuarterSelectionDialogVisible by remember { mutableStateOf(false) }
        var sharingQuarters by remember { mutableStateOf(emptyList<Int>()) }

        var isCapturing by remember { mutableStateOf(false) }
        var capturedUris by remember { mutableStateOf(mapOf<Int, Uri>()) }
        var totalQuartersToCapture by remember { mutableIntStateOf(0) }
        val multipleShareQuarterSelectionAlert = stringResource(R.string.multiple_share_quarter_selection_alert)
        val multipleShareCaptureError = stringResource(R.string.multiple_share_capture_error)

        var isSaveDialogVisible by remember { mutableStateOf(false) }
        var toastMessage by remember { mutableStateOf<String?>(null) }
        val formationSaveAlert = stringResource(R.string.formation_save_alert)
        val loadPlayerFailedServerConnection = stringResource(R.string.load_player_failed_server_connection)

        var availablePlayers by remember { mutableStateOf(emptyList<TeamPlayerModel>()) }
        var playerAssignmentState by remember { mutableStateOf(PlayerAssignmentState()) }

        var deleteConfirmationState by remember { mutableStateOf(DeleteConfirmationState()) }

        var sideEffect by remember { mutableStateOf<FormationSideEffect?>(null) }

        var isPlayerQuarterStatusVisible by remember { mutableStateOf(false) }
        var playerQuarterStatus by remember { mutableStateOf(emptyList<PlayerQuarterStatusModel>()) }

        fun calculatePlayerQuarterStatus(allPlacements: Map<Int, List<PlacementModel>>): List<PlayerQuarterStatusModel> {
            val allPlacements = allPlacements.flatMap { (quarter, placements) ->
                placements.mapNotNull { placement ->
                    placement.playerId?.let {
                        Triple(it, quarter, placement.playerName)
                    }
                }
            }

            return allPlacements
                .groupBy { it.first }
                .map { (playerId, placements) ->
                    val playerInfo = availablePlayers.find { it.id == playerId }

                    val playerName = playerInfo?.name ?: "Unknown Player"
                    val backNumber = playerInfo?.backNumber ?: 0
                    val position = playerInfo?.position ?: "Unknown Position"
                    val quarters = placements.map { it.second }.distinct().sorted()

                    PlayerQuarterStatusModel(
                        playerId = playerId,
                        playerName = playerName,
                        quarters = quarters,
                        backNumber = backNumber,
                        position = position
                    )
                }
                .sortedByDescending { it.quarters.size }
        }

        LaunchedEffect(Unit) {
            playerRepository.getTeamPlayers(screen.teamId)
                .onSuccess {
                    availablePlayers = it
                }
                .onFailure {
                    toastMessage = loadPlayerFailedServerConnection
                }

            allPlacements = mapOf(
                1 to createDefaultPlayers(1),
                2 to createDefaultPlayers(2),
                3 to createDefaultPlayers(3),
                4 to createDefaultPlayers(4)
            )

            players = allPlacements[currentQuarter]!!
        }

        fun handleCaptureComplete(quarter: Int, uri: Uri?) {
            if (uri != null) {
                capturedUris = capturedUris + (quarter to uri)
            }

            val remainingQuarters = sharingQuarters.filter { it > quarter }.sorted()

            if (remainingQuarters.isNotEmpty()) {
                val nextQuarter = remainingQuarters.first()

                currentQuarter = nextQuarter
                players = allPlacements[nextQuarter]!!

                sideEffect = FormationSideEffect.CaptureFormation(
                    quarter = nextQuarter,
                    onCaptureUri = ::handleCaptureComplete
                )
            } else {
                isCapturing = false

                val urisToSend = sharingQuarters.mapNotNull { capturedUris[it] }

                if (urisToSend.size == totalQuartersToCapture && urisToSend.isNotEmpty()) {
                    sideEffect = FormationSideEffect.ShareMultipleImages(urisToSend)
                } else {
                    toastMessage = multipleShareCaptureError
                }
            }
        }

        fun handleEvent(event: FormationUiEvent) {
            when (event) {
                is FormationUiEvent.OnFormationShareClick -> {
                    isQuarterSelectionDialogVisible = true
                }

                is FormationUiEvent.OnSelectQuartersToShare -> {
                    isQuarterSelectionDialogVisible = false
                    val quarterList = event.quarters.sorted().toList()

                    if (quarterList.isNotEmpty()) {
                        sharingQuarters = quarterList
                        totalQuartersToCapture = quarterList.size
                        capturedUris = emptyMap()
                        isCapturing = true
                        currentQuarter = quarterList.first()
                        players = allPlacements[currentQuarter]!!

                        sideEffect = FormationSideEffect.CaptureFormation(
                            quarter = currentQuarter,
                            onCaptureUri = ::handleCaptureComplete
                        )
                    } else {
                        toastMessage = multipleShareQuarterSelectionAlert
                    }
                }

                FormationUiEvent.OnDismissQuarterSelectionDialog -> {
                    isQuarterSelectionDialogVisible = false
                }

                FormationUiEvent.OnBackButtonClick -> {
                    navigator.pop()
                }

                FormationUiEvent.OnFormationResetClick -> {
                    isResetConfirmDialogVisible = true
                }

                is FormationUiEvent.OnQuarterChange -> {
                    currentQuarter = event.quarter
                    players = allPlacements[currentQuarter] ?: createDefaultPlayers(currentQuarter)
                    selectedSlotId = null
                }

                FormationUiEvent.OnConfirmReset -> {
                    allPlacements = mapOf(
                        1 to createDefaultPlayers(1),
                        2 to createDefaultPlayers(2),
                        3 to createDefaultPlayers(3),
                        4 to createDefaultPlayers(4)
                    )
                    players = allPlacements[currentQuarter]!!

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

                FormationUiEvent.OnPlayerQuarterStatusClick -> {
                    if (!isPlayerQuarterStatusVisible) {
                        playerQuarterStatus = calculatePlayerQuarterStatus(allPlacements)
                    }
                    isPlayerQuarterStatusVisible = !isPlayerQuarterStatusVisible
                }

                is FormationUiEvent.OnFormationCardClick -> {
                    scope.launch {
                        formationRepository.getFormationDetail(event.formationId)
                            .onSuccess { formationDetail ->
                                val loadedPlacements = formationDetail.placements.groupBy { it.quarter }
                                val initialPlacements = (1..4).associateWith { createDefaultPlayers(it) }
                                allPlacements = initialPlacements + loadedPlacements

                                players = allPlacements[currentQuarter] ?: createDefaultPlayers(currentQuarter)

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
                    val isCurrentQuarterFullyAssigned = players.size == 11 &&
                            players.all { it.playerId != null }

                    if (isCurrentQuarterFullyAssigned) {
                        isSaveDialogVisible = true
                    } else {
                        toastMessage = formationSaveAlert
                    }
                }

                FormationUiEvent.OnSaveDialogConfirm -> {
                    scope.launch {
                        val isUpdate = currentFormationId != null
                        val placements = allPlacements.flatMap { (quarter, players) ->
                            players.mapNotNull { placement ->
                                placement.playerId?.let {
                                    PlacementSaveModel(
                                        playerId = it,
                                        quarter = quarter,
                                        coordX = (placement.coordX * 1000).toInt(),
                                        coordY = (placement.coordY * 1000).toInt()
                                    )
                                }
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

                    allPlacements = allPlacements + (currentQuarter to players)
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

                    allPlacements = allPlacements + (currentQuarter to players)
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

                    allPlacements = allPlacements + (currentQuarter to players)
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

                    allPlacements = allPlacements + (currentQuarter to players)
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
            currentQuarter = currentQuarter,
            formationList = formationList,
            isListModalVisible = isListModalVisible,
            isQuarterSelectionDialogVisible = isQuarterSelectionDialogVisible,
            isPlayerQuarterStatusVisible = isPlayerQuarterStatusVisible,
            playerQuarterStatus = playerQuarterStatus,
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
            isCapturing = isCapturing,
            totalQuartersToCapture = totalQuartersToCapture,
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
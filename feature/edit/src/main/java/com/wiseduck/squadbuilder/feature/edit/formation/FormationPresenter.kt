package com.wiseduck.squadbuilder.feature.edit.formation

import android.net.Uri
import android.util.Log
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
import com.wiseduck.squadbuilder.core.common.analytics.AnalyticsService
import com.wiseduck.squadbuilder.core.common.utils.UiText
import com.wiseduck.squadbuilder.core.common.utils.handleException
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.launch

class FormationPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: FormationScreen,
    private val formationRepository: FormationRepository,
    private val playerRepository: PlayerRepository,
    private val analyticsService: AnalyticsService,
) : Presenter<FormationUiState> {

    @CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: FormationScreen,
            navigator: Navigator,
        ): FormationPresenter
    }

    private companion object {
        const val FORMATION_SHARE_SUCCESS = "formation_share_success"
        const val TOTAL_IMAGES = "total_images"
    }

    @Composable
    override fun present(): FormationUiState {
        val scope = rememberCoroutineScope()
        var sideEffect by remember { mutableStateOf<FormationSideEffect?>(null) }

        var isLoading by remember { mutableStateOf(false) }
        var isCapturing by remember { mutableStateOf(false) }
        var isSaveDialogVisible by remember { mutableStateOf(false) }
        var isListModalVisible by remember { mutableStateOf(false) }
        var isResetConfirmDialogVisible by remember { mutableStateOf(false) }
        var isPlayerQuarterStatusVisible by remember { mutableStateOf(false) }
        var isQuarterSelectionDialogVisible by remember { mutableStateOf(false) }

        val teamId = screen.teamId
        val teamName = screen.teamName
        var allPlacements by remember {
            mutableStateOf(persistentMapOf<Int, PersistentList<PlacementModel>>())
        }
        var allReferees by remember { mutableStateOf(persistentMapOf<Int, String>()) }
        var formationList by remember { mutableStateOf(persistentListOf<FormationListItemModel>()) }
        var players by remember { mutableStateOf(persistentListOf<PlacementModel>()) }
        var availablePlayers by remember { mutableStateOf(persistentListOf<TeamPlayerModel>()) }
        var selectedSlotId by remember { mutableStateOf<Int?>(null) }
        var playerQuarterStatus by remember { mutableStateOf<ImmutableList<PlayerQuarterStatusModel>>(persistentListOf()) }

        var currentQuarter by remember { mutableIntStateOf(1) }
        var currentFormationId by remember { mutableStateOf<Int?>(null) }
        var currentFormationName by remember { mutableStateOf("") }
        val formationDefaultName = stringResource(R.string.formation_default_name)
        var sharingQuarters by remember { mutableStateOf(emptyList<Int>()) }
        var capturedUris by remember { mutableStateOf(mapOf<Int, Uri>()) }
        var totalQuartersToCapture by remember { mutableIntStateOf(0) }
        var currentCaptureQuarter by remember { mutableStateOf<Int?>(null) }

        var draggedPlayerInitialPosition by remember { mutableStateOf<PlacementModel?>(null) }
        var playerAssignmentState by remember { mutableStateOf(PlayerAssignmentState()) }
        var deleteConfirmationState by remember { mutableStateOf(DeleteConfirmationState()) }

        LaunchedEffect(Unit) {
            playerRepository.getTeamPlayers(screen.teamId)
                .onSuccess {
                    availablePlayers = it.toPersistentList()
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = FormationSideEffect.ShowToast(it) })
                }

            allPlacements = persistentMapOf(
                1 to createDefaultPlayers(1).toPersistentList(),
                2 to createDefaultPlayers(2).toPersistentList(),
                3 to createDefaultPlayers(3).toPersistentList(),
                4 to createDefaultPlayers(4).toPersistentList(),
            )

            players = allPlacements[currentQuarter]!!
        }

        fun calculatePlayerQuarterStatus(
            allPlacements: PersistentMap<Int, PersistentList<PlacementModel>>,
        ): ImmutableList<PlayerQuarterStatusModel> {
            val flatPlacements = allPlacements.flatMap { (quarter, placements) ->
                placements.mapNotNull { placement ->
                    placement.playerId?.let {
                        Triple(it, quarter, placement.playerName)
                    }
                }
            }

            return flatPlacements
                .groupBy { it.first }
                .map { (playerId, placements) ->
                    val playerInfo = availablePlayers.find { it.id == playerId }

                    val playerName = playerInfo?.name ?: "Unknown Player"
                    val backNumber = playerInfo?.backNumber ?: 0
                    val position = playerInfo?.position ?: "Unknown Position"
                    val quarters = placements.map { it.second }.distinct().sorted().toPersistentList()

                    PlayerQuarterStatusModel(
                        playerId = playerId,
                        playerName = playerName,
                        quarters = quarters,
                        backNumber = backNumber,
                        position = position,
                    )
                }
                .sortedByDescending { it.quarters.size }
                .toPersistentList()
        }

        fun onQuarterCaptureFinished(quarter: Int, uri: Uri?) {
            if (uri != null) {
                capturedUris = capturedUris + (quarter to uri)
            }

            currentCaptureQuarter = null

            val remainingQuarters = sharingQuarters.filter { it > quarter }.sorted()

            if (remainingQuarters.isNotEmpty()) {
                val nextQuarter = remainingQuarters.first()

                currentQuarter = nextQuarter
                players = allPlacements[nextQuarter]!!

                currentCaptureQuarter = nextQuarter
            } else {
                isCapturing = false
                val urisToSend = sharingQuarters.mapNotNull { capturedUris[it] }

                if (urisToSend.size == totalQuartersToCapture && urisToSend.isNotEmpty()) {
                    analyticsService.logEvent(FORMATION_SHARE_SUCCESS, params =
                        mapOf(
                            TOTAL_IMAGES to urisToSend.size,
                        ),
                    )
                    sideEffect = FormationSideEffect.ShareMultipleImages(urisToSend.toPersistentList())
                } else {
                    sideEffect = FormationSideEffect.ShowToast(UiText.StringResource(R.string.multiple_share_capture_error))
                }
            }
        }

        fun resetFormation() {
            allPlacements = persistentMapOf(
                1 to createDefaultPlayers(1).toPersistentList(),
                2 to createDefaultPlayers(2).toPersistentList(),
                3 to createDefaultPlayers(3).toPersistentList(),
                4 to createDefaultPlayers(4).toPersistentList(),
            )
            players = allPlacements[currentQuarter]!!

            currentFormationId = null
            currentFormationName = ""
            isResetConfirmDialogVisible = false
        }

        suspend fun getFormationList() {
            formationRepository.getFormationList(teamId)
                .onSuccess { list ->
                    formationList = list.toPersistentList()
                    isListModalVisible = true
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = FormationSideEffect.ShowToast(it) })
                }
        }

        fun buildFormationSaveRequest(formationDefaultName: String): FormationSaveModel {
            val placements =
                allPlacements.flatMap { (quarter, players) ->
                    players.mapNotNull { placement ->
                        placement.playerId?.let { playerId ->
                            PlacementSaveModel(playerId, quarter, (placement.coordX * 1000).toInt(), (placement.coordY * 1000).toInt())
                        }
                    }
                }

            val refereesMap = allReferees.mapKeys { (quarter, _) ->
                quarter.toString()
            }

            return FormationSaveModel(
                teamId = teamId,
                name = currentFormationName.ifBlank { formationDefaultName },
                placements = placements,
                referees = refereesMap,
            )
        }

        suspend fun createFormation(request: FormationSaveModel) {
            formationRepository.createFormation(request)
                .onSuccess {
                    sideEffect = FormationSideEffect.ShowToast(UiText.StringResource(R.string.success_toast_message))
                    formationRepository.getFormationList(teamId)
                        .onSuccess { formationList = it.toPersistentList() }
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = FormationSideEffect.ShowToast(it) })
                }
        }

        suspend fun updateFormation(request: FormationSaveModel) {
            formationRepository.updateFormation(currentFormationId!!, request)
                .onSuccess {
                    sideEffect = FormationSideEffect.ShowToast(UiText.StringResource(R.string.success_toast_message))
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = FormationSideEffect.ShowToast(it) })
                }
        }

        suspend fun deleteFormation(formationId: Int) {
            formationRepository.deleteFormation(formationId)
                .onSuccess {
                    sideEffect = FormationSideEffect.ShowToast(UiText.StringResource(R.string.success_toast_message))
                    formationList = formationList.mutate { list ->
                        list.removeIf { it.formationId == formationId }
                    }
                    if (currentFormationId == formationId) {
                        currentFormationId = null
                        currentFormationName = ""
                    }
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = FormationSideEffect.ShowToast(it)})
                }
        }

        suspend fun changeFormation(formationId: Int) {
            formationRepository.getFormationDetail(formationId)
                .onSuccess { formationDetail ->
                    val loadedPlacements = formationDetail.placements
                        .groupBy { it.quarter }
                        .mapValues { it.value.toPersistentList() }

                    val initialPlacements = (1..4).associateWith {
                        createDefaultPlayers(it).toPersistentList()
                    }.toPersistentMap()

                    allPlacements = (initialPlacements + loadedPlacements).toPersistentMap()

                    players = allPlacements[currentQuarter] ?: initialPlacements[currentQuarter]!!

                    allReferees = formationDetail.referees
                        .mapKeys { (quarter, _) -> quarter.toIntOrNull() ?: 0 }
                        .filterKeys { it != 0 }
                        .toPersistentMap()

                    currentFormationId = formationDetail.formationId
                    currentFormationName = formationDetail.name
                    isListModalVisible = false
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = FormationSideEffect.ShowToast(it) })
                }
        }

        fun handleEvent(event: FormationUiEvent) {
            when (event) {
                is FormationUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                FormationUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                FormationUiEvent.OnFormationResetClick -> {
                    isResetConfirmDialogVisible = true
                }

                is FormationUiEvent.OnFormationShareClick -> {
                    isQuarterSelectionDialogVisible = true
                }

                FormationUiEvent.OnFormationSaveClick -> {
                    val isCurrentQuarterFullyAssigned =
                        players.size == 11 &&
                            players.all { it.playerId != null }

                    if (isCurrentQuarterFullyAssigned) {
                        isSaveDialogVisible = true
                    } else {
                        sideEffect = FormationSideEffect.ShowToast(UiText.StringResource(R.string.formation_save_alert))
                    }
                }

                FormationUiEvent.OnPlayerQuarterStatusClick -> {
                    if (!isPlayerQuarterStatusVisible) {
                        playerQuarterStatus = calculatePlayerQuarterStatus(allPlacements)
                    }
                    isPlayerQuarterStatusVisible = !isPlayerQuarterStatusVisible
                }

                is FormationUiEvent.OnSelectQuartersToShare -> {
                    isQuarterSelectionDialogVisible = false
                    val quarterList = event.quarters.sorted().toList()

                    if (quarterList.isNotEmpty()) {
                        Log.d("SHARE_IMAGE", "handleEvent: $quarterList")
                        sharingQuarters = quarterList
                        totalQuartersToCapture = quarterList.size
                        capturedUris = emptyMap()
                        isCapturing = true
                        currentQuarter = quarterList.first()
                        players = allPlacements[currentQuarter]!!

                        currentCaptureQuarter = currentQuarter
                    } else {
                        sideEffect = FormationSideEffect.ShowToast(UiText.StringResource(R.string.multiple_share_quarter_selection_alert))
                    }
                }

                is FormationUiEvent.OnCaptureComplete -> {
                    onQuarterCaptureFinished(event.quarter, event.uri)
                }

                FormationUiEvent.OnFormationResetConfirm -> {
                    resetFormation()
                }

                is FormationUiEvent.OnQuarterChange -> {
                    currentQuarter = event.quarter
                    players = allPlacements[currentQuarter] ?: createDefaultPlayers(currentQuarter).toPersistentList()
                    selectedSlotId = null
                }

                FormationUiEvent.OnFormationListClick -> {
                    scope.launch {
                        getFormationList()
                    }
                }

                is FormationUiEvent.OnFormationCardClick -> {
                    scope.launch {
                        changeFormation(event.formationId)
                    }
                }

                is FormationUiEvent.OnFormationNameChange -> {
                    currentFormationName = event.name
                }

                is FormationUiEvent.OnRefereeNameChange -> {
                    allReferees = allReferees.put(event.quarter, event.refereeName)
                }

                is FormationUiEvent.OnPlayerClick -> {
                    val clickedSlot = players.find { it.slotId == event.slotId }
                    if (clickedSlot != null) {
                        if (clickedSlot.playerId == null) {
                            playerAssignmentState = PlayerAssignmentState(
                                isDialogVisible = true,
                                slotId = clickedSlot.slotId,
                            )
                        } else {
                            selectedSlotId = if (selectedSlotId == event.slotId) null else event.slotId
                        }
                    }
                }

                is FormationUiEvent.OnPlayerDragStart -> {
                    draggedPlayerInitialPosition = players.find { it.slotId == event.slotId }
                }

                is FormationUiEvent.OnPlayerDrag -> {
                    players = players.mutate { list ->
                        val index = list.indexOfFirst { it.slotId == event.slotId }
                        if (index != -1) {
                            val player = list[index]
                            list[index] = player.copy(
                                coordX = (player.coordX + event.deltaCoordX).coerceIn(0f, 1f),
                                coordY = (player.coordY + event.deltaCoordY).coerceIn(0f, 1f),
                            )
                        }
                    }

                    allPlacements = allPlacements.put(currentQuarter, players)
                }

                is FormationUiEvent.OnPlayerDragEnd -> {
                    val draggedPlayer = players.find { it.slotId == event.slotId }
                    val initialPos = draggedPlayerInitialPosition

                    if (draggedPlayer != null && initialPos != null) {
                        val overlappedPlayer =
                            players.firstOrNull { otherPlayer ->
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

                        players = players.mutate { list ->
                            if (overlappedPlayer != null) {
                                val dIdx = list.indexOfFirst { it.slotId == draggedPlayer.slotId }
                                val oIdx = list.indexOfFirst { it.slotId == overlappedPlayer.slotId }
                                if (dIdx != -1 && oIdx != -1) {
                                    list[oIdx] = list[oIdx].copy(
                                        coordX = initialPos.coordX,
                                        coordY = initialPos.coordY,
                                        playerPosition = getPositionForCoordinates(initialPos.coordX, initialPos.coordY),
                                    )
                                    list[dIdx] = list[dIdx].copy(
                                        coordX = overlappedPlayer.coordX,
                                        coordY = overlappedPlayer.coordY,
                                        playerPosition = getPositionForCoordinates(overlappedPlayer.coordX, overlappedPlayer.coordY),
                                    )
                                }
                            } else {
                                val index = list.indexOfFirst { it.slotId == draggedPlayer.slotId }
                                if (index != -1) {
                                    list[index] = list[index].copy(
                                        playerPosition = getPositionForCoordinates(list[index].coordX, list[index].coordY),
                                    )
                                }
                            }
                        }
                    }
                    draggedPlayerInitialPosition = null
                    allPlacements = allPlacements.put(currentQuarter, players)
                }

                FormationUiEvent.OnModifyPlayerClick -> {
                    if (selectedSlotId != null) {
                        playerAssignmentState = PlayerAssignmentState(isDialogVisible = true, slotId = selectedSlotId)
                    }
                    selectedSlotId = null
                }

                is FormationUiEvent.OnAssignPlayer -> {
                    val targetSlotId = playerAssignmentState.slotId
                    val playerToAssign = availablePlayers.find { it.id == event.playerIdToAssign }

                    if (targetSlotId != null && playerToAssign != null) {
                        players = players.mutate { list ->
                            val index = list.indexOfFirst { it.slotId == targetSlotId }
                            if (index != -1) {
                                list[index] = list[index].copy(
                                    playerId = playerToAssign.id,
                                    playerName = playerToAssign.name,
                                    playerBackNumber = playerToAssign.backNumber.toString(),
                                )
                            }
                        }
                    }
                    playerAssignmentState = PlayerAssignmentState(isDialogVisible = false, slotId = null)

                    allPlacements = allPlacements.put(currentQuarter, players)
                }

                FormationUiEvent.OnUnassignPlayer -> {
                    val targetSlotId = selectedSlotId
                    if (targetSlotId != null) {
                        players = players.mutate { list ->
                            val index = list.indexOfFirst { it.slotId == targetSlotId }
                            if (index != -1) {
                                list[index] = list[index].copy(
                                    playerId = null,
                                    playerName = "Player",
                                    playerBackNumber = "+",
                                )
                            }
                        }
                    }
                    selectedSlotId = null
                    allPlacements = allPlacements.put(currentQuarter, players)
                }

                is FormationUiEvent.OnFormationDeleteClick -> {
                    deleteConfirmationState = DeleteConfirmationState(isDialogVisible = true, formationIdToDelete = event.formationId)
                }

                FormationUiEvent.OnFormationDeleteConfirm -> {
                    val formationId = deleteConfirmationState.formationIdToDelete

                    if (formationId != null) {
                        scope.launch {
                            deleteFormation(formationId)
                        }
                    }

                    deleteConfirmationState = DeleteConfirmationState()
                }

                FormationUiEvent.OnFormationSaveConfirm -> {
                    isSaveDialogVisible = false
                    scope.launch {
                        isLoading = true
                        try {
                            val request = buildFormationSaveRequest(formationDefaultName)

                            if (currentFormationId != null) {
                                updateFormation(request)
                            } else {
                                createFormation(request)
                            }
                        } finally {
                            isLoading = false
                        }
                    }
                }

                FormationUiEvent.OnDismissPlayerAssignmentDialog -> {
                    playerAssignmentState = PlayerAssignmentState(isDialogVisible = false, slotId = null)
                }

                FormationUiEvent.OnDismissFormationDeleteDialog -> {
                    deleteConfirmationState = DeleteConfirmationState()
                }

                FormationUiEvent.OnDismissPlayerInfoDialog -> {
                    selectedSlotId = null
                }

                FormationUiEvent.OnDismissFormationListModal -> {
                    isListModalVisible = false
                }

                FormationUiEvent.OnDismissFormationResetDialog -> {
                    isResetConfirmDialogVisible = false
                }

                FormationUiEvent.OnDismissFormationSaveDialog -> {
                    isSaveDialogVisible = false
                }

                FormationUiEvent.OnDismissQuarterSelectionDialog -> {
                    isQuarterSelectionDialogVisible = false
                }
            }
        }

        val assignedPlayerIds = players.mapNotNull { it.playerId }.toSet()
        val filteredAvailablePlayers = availablePlayers.filter { it.id !in assignedPlayerIds }.toPersistentList()

        return FormationUiState(
            teamId = teamId,
            teamName = teamName,
            isLoading = isLoading,
            currentQuarter = currentQuarter,
            allReferees = allReferees,
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
            availablePlayers = filteredAvailablePlayers,
            playerAssignmentState = playerAssignmentState,
            deleteConfirmationState = deleteConfirmationState,
            isCapturing = isCapturing,
            totalQuartersToCapture = totalQuartersToCapture,
            currentCaptureQuarter = currentCaptureQuarter,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

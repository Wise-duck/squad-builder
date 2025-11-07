package com.wiseduck.squadbuilder.feature.edit.formation

import android.widget.Toast
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.PlayerChip
import com.wiseduck.squadbuilder.core.ui.component.SoccerField
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationController
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationHeader
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationListModal
import com.wiseduck.squadbuilder.feature.edit.formation.component.PlayerAssignmentModal
import com.wiseduck.squadbuilder.feature.edit.formation.component.PlayerInfoModal
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers
import com.wiseduck.squadbuilder.feature.edit.formation.data.getPositionForCoordinates
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer

@CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
@Composable
fun FormationUi(
    state: FormationUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val formationGraphicLayers = rememberGraphicsLayer()

    FormationSideEffects(
        state = state,
        formationGraphicsLayer = formationGraphicLayers
    )

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            state.eventSink(FormationUiEvent.OnToastShown)
        }
    }

    if (state.deleteConfirmationState.isDialogVisible) {
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnDeleteFormationConfirm) },
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissDeleteDialog) },
            confirmButtonText = "삭제",
            dismissButtonText = "취소",
            title = "포메이션 삭제",
            content = {
                Text(
                    text = "정말로 이 포메이션을 삭제하시겠습니까?",
                    color = Color.White
                )
            }
        )
    }

    if (state.playerAssignmentState.isDialogVisible) {
        PlayerAssignmentModal(
            availablePlayers = state.availablePlayers,
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissPlayerAssignmentDialog) },
            onAssignPlayer = { state.eventSink(FormationUiEvent.OnAssignPlayer(it)) }
        )
    }

    if (state.isSaveDialogVisible) {
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnSaveDialogConfirm) },
            onDismissRequest = { state.eventSink(FormationUiEvent.OnSaveDialogDismiss) },
            confirmButtonText = "저장",
            dismissButtonText = "취소",
            title = "포메이션 저장",
            content = {
                TextField(
                    value = state.currentFormationName,
                    onValueChange = { state.eventSink(FormationUiEvent.OnFormationNameChange(it)) },
                    placeholder = { Text("포메이션 이름을 입력하세요") },
                    maxLines = 1,
                )
            }
        )
    }

    if (state.isResetConfirmDialogVisible) {
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnConfirmReset) },
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissResetDialog) },
            confirmButtonText = "확인",
            dismissButtonText = "취소",
            title = "포메이션 초기화",
            content = {
                Text(
                    text = "정말로 포메이션을 초기화하시겠습니까?",
                    color = Color.White
                )
            }
        )
    }

    val selectedPlayer = state.players.find { it.slotId == state.selectedSlotId }
    if (selectedPlayer != null && selectedPlayer.playerId != null) {
        PlayerInfoModal(
            playerName = selectedPlayer.playerName,
            playerPosition = selectedPlayer.playerPosition,
            playerBackNumber = selectedPlayer.playerBackNumber,
            onModifyClick = { state.eventSink(FormationUiEvent.OnModifyPlayerClick) },
            onUnassignClick = { state.eventSink(FormationUiEvent.OnUnassignPlayer) },
            onCancelClick = { state.eventSink(FormationUiEvent.OnDismissPlayerInfoDialog) }
        )
    }

    if (state.isListModalVisible) {
        FormationListModal(
            formationList = state.formationList,
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissListModal) },
            onFormationCardClick = { state.eventSink(FormationUiEvent.OnFormationCardClick(it)) },
            onDeleteFormationClick = { state.eventSink(FormationUiEvent.OnDeleteFormationClick(it)) }
        )
    }
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            FormationHeader(
                modifier = Modifier,
                onBackClick = {
                    state.eventSink(FormationUiEvent.OnBackButtonClick)
                },
                onFormationListClick = {
                    state.eventSink(FormationUiEvent.OnFormationListClick)
                }
            )

            FormationController(
                teamName = state.teamName,
                onFormationResetClick = { state.eventSink(FormationUiEvent.OnFormationResetClick) },
                onFormationShareClick = { state.eventSink(FormationUiEvent.OnFormationShareClick) },
                onFormationSaveClick = { state.eventSink(FormationUiEvent.OnFormationSaveClick) },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                SoccerField(
                    modifier = Modifier
                        .fillMaxSize()
                        .captureToGraphicsLayer(formationGraphicLayers),
                    content = {
                        val centerCircleRadius = this.maxWidth * 0.15f
                        val desiredShirtDiameter = centerCircleRadius * 0.9f

                        val originalShirtDiameter = 40.dp
                        val scaleFactor = desiredShirtDiameter / originalShirtDiameter

                        state.players.forEach { player ->
                            key(player.slotId) {
                                val originalChipWidth = 56.dp
                                val originalChipHeight = 64.dp

                                val calculatedX = (this.maxWidth * player.coordX) - (originalChipWidth / 2)
                                val calculatedY = (this.maxHeight * player.coordY) - (originalChipHeight / 2)

                                val xOffset = calculatedX.coerceIn(0.dp, this.maxWidth - originalChipWidth)
                                val yOffset = calculatedY.coerceIn(0.dp, this.maxHeight - originalChipHeight)

                                Column(
                                    modifier = Modifier
                                        .offset(x = xOffset, y = yOffset),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = getPositionForCoordinates(player.coordX, player.coordY),
                                        color = Color.White
                                    )
                                    PlayerChip(
                                        modifier = Modifier
                                            .scale(scaleFactor)
                                            .pointerInput(player.slotId) {
                                                coroutineScope {
                                                    launch {
                                                        detectDragGestures(
                                                            onDragStart = {
                                                                state.eventSink(
                                                                    FormationUiEvent.OnPlayerDragStart(
                                                                        player.slotId
                                                                    )
                                                                )
                                                            },
                                                            onDrag = { change, dragAmount ->
                                                                change.consume()

                                                                val fieldWidthPx =
                                                                    this@SoccerField.maxWidth.toPx()
                                                                val fieldHeightPx =
                                                                    this@SoccerField.maxHeight.toPx()

                                                                val deltaX = dragAmount.x / fieldWidthPx
                                                                val deltaY = dragAmount.y / fieldHeightPx

                                                                state.eventSink(
                                                                    FormationUiEvent.OnPlayerDrag(
                                                                        slotId = player.slotId,
                                                                        deltaCoordX = deltaX,
                                                                        deltaCoordY = deltaY
                                                                    )
                                                                )
                                                            },
                                                            onDragEnd = {
                                                                val fieldWidthPx =
                                                                    this@SoccerField.maxWidth.toPx()
                                                                val fieldHeightPx =
                                                                    this@SoccerField.maxHeight.toPx()

                                                                val relativeChipWidth =
                                                                    (originalChipWidth.toPx() * scaleFactor) / fieldWidthPx
                                                                val relativeChipHeight =
                                                                    (originalChipHeight.toPx() * scaleFactor) / fieldHeightPx

                                                                state.eventSink(
                                                                    FormationUiEvent.OnPlayerDragEnd(
                                                                        slotId = player.slotId,
                                                                        relativeChipWidth = relativeChipWidth,
                                                                        relativeChipHeight = relativeChipHeight
                                                                    )
                                                                )
                                                            }
                                                        )
                                                    }
                                                    launch {
                                                        detectTapGestures(
                                                            onTap = {
                                                                state.eventSink(
                                                                    FormationUiEvent.OnPlayerClick(
                                                                        player.slotId
                                                                    )
                                                                )
                                                            }
                                                        )
                                                    }
                                                }
                                            },
                                        position = player.playerPosition,
                                        number = player.playerBackNumber,
                                        name = player.playerName
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

fun Modifier.captureToGraphicsLayer(graphicsLayer: GraphicsLayer) =
    this.drawWithContent {
        graphicsLayer.record { this@drawWithContent.drawContent() }
        drawLayer(graphicsLayer)
    }

@Composable
private fun FormationContent(
    modifier: Modifier = Modifier,
    state: FormationUiState
) {}

@DevicePreview
@Composable
private fun FormationUiPreView() {
    SquadBuilderTheme {

        val dummyPlayers = createDefaultPlayers()

        FormationUi(
            state = FormationUiState(
                teamId = 1,
                teamName = "비안코",
                players = dummyPlayers,
                eventSink = {}
            )
        )
    }
}

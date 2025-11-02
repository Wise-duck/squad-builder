package com.wiseduck.squadbuilder.feature.edit.formation

import android.widget.Toast
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationCard
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationController
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationHeader
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers
import com.wiseduck.squadbuilder.feature.edit.formation.data.getPositionForCoordinates
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
@Composable
fun FormationUi(
    state: FormationUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
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
        SquadBuilderDialog(
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissPlayerAssignmentDialog) },
            title = "선수 배정",
            confirmButtonText = "닫기",
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnDismissPlayerAssignmentDialog) },
            content = {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.availablePlayers) { player ->
                        // TODO: PlayerCard 컴포저블 만들기
                        Card(
                            onClick = { state.eventSink(FormationUiEvent.OnAssignPlayer(player.id)) }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = player.name)
                                Text(text = "#${player.backNumber}")
                                Text(text = player.position)
                            }
                        }
                    }
                }
            }
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
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnModifyPlayerClick) },
            onDismissRequest = { state.eventSink(FormationUiEvent.OnUnassignPlayer) },
            confirmButtonText = "수정",
            dismissButtonText = "배정 해제",
            title = "선수 정보",
            content = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = selectedPlayer.playerPosition,
                        color = Color.White
                    )
                    Text(
                        text = selectedPlayer.playerName,
                        color = Color.White
                    )
                    Text(
                        text = selectedPlayer.playerBackNumber,
                        color = Color.White
                    )
                }
            }
        )
    }

    if (state.isListModalVisible) {
        SquadBuilderDialog(
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissListModal) },
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnDismissListModal) },
            confirmButtonText = "닫기",
            dismissButtonText = null,
            title = "포메이션 목록",
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.formationList) { formationItem ->
                        FormationCard(
                            formation = formationItem,
                            onClick = { state.eventSink(FormationUiEvent.OnFormationCardClick(formationItem.formationId)) },
                            onDeleteClick = { state.eventSink(FormationUiEvent.OnDeleteFormationClick(formationItem.formationId)) }
                        )
                    }
                }
            }
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
                }
            )

            FormationController(
                teamName = state.teamName,
                onFormationResetClick = { state.eventSink(FormationUiEvent.OnFormationResetClick) },
                onFormationListClick = { state.eventSink(FormationUiEvent.OnFormationListClick) },
                onFormationSaveClick = { state.eventSink(FormationUiEvent.OnFormationSaveClick) }

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
                        .fillMaxSize(),
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

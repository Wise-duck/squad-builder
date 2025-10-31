package com.wiseduck.squadbuilder.feature.edit.formation

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.PlayerChip
import com.wiseduck.squadbuilder.core.ui.component.SoccerField
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationCard
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationController
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationHeader
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
@Composable
fun FormationUi(
    state: FormationUiState,
    modifier: Modifier = Modifier,
) {
    val selectedPlayer = state.players.find { it.playerId == state.selectedPlayerId }
    if (selectedPlayer != null) {
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnDismissPlayerInfoDialog) },
            onDismissRequest = { /* TODO: 삭제 로직 */ },
            confirmButtonText = "취소",
            dismissButtonText = "삭제",
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
                            onClick = {},
                            onDeleteClick = {}
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
            modifier = modifier.padding(innerPadding)
        ) {
            FormationHeader(
                modifier = modifier,
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
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                SoccerField(
                    modifier = modifier
                        .fillMaxSize(),
                    content = {
                        val centerCircleRadius = this.maxWidth * 0.15f
                        val desiredShirtDiameter = centerCircleRadius * 0.9f

                        val originalShirtDiameter = 40.dp
                        val scaleFactor = desiredShirtDiameter / originalShirtDiameter

                        state.players.forEach { player ->
                            val originalChipWidth = 56.dp
                            val originalChipHeight = 64.dp

                            val calculatedX = (this.maxWidth * player.coordX) - (originalChipWidth / 2)
                            val calculatedY = (this.maxHeight * player.coordY) - (originalChipHeight / 2)

                            val xOffset = calculatedX.coerceIn(0.dp, this.maxWidth - originalChipWidth)
                            val yOffset = calculatedY.coerceIn(0.dp, this.maxHeight - originalChipHeight)

                            PlayerChip(
                                modifier = modifier
                                    .offset(x = xOffset, y = yOffset)
                                    .scale(scaleFactor)
                                    .pointerInput(player.playerId) {
                                        detectTapGestures(
                                            onTap = {
                                                state.eventSink(
                                                    FormationUiEvent.OnPlayerClick(
                                                        player.playerId
                                                    )
                                                )
                                            }
                                        )
                                    },
                                position = player.playerPosition,
                                number = player.playerBackNumber,
                                name = player.playerName
                            )
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

package com.wiseduck.squadbuilder.feature.edit.formation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.mediumRoundedButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral900
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.SoccerField
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationController
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationHeader
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationListModal
import com.wiseduck.squadbuilder.feature.edit.formation.component.PlayerAssignmentModal
import com.wiseduck.squadbuilder.feature.edit.formation.component.PlayerInfoModal
import com.wiseduck.squadbuilder.feature.edit.formation.component.PlayerPlacementLayer
import com.wiseduck.squadbuilder.feature.edit.formation.component.PlayerQuarterStatusSideBar
import com.wiseduck.squadbuilder.feature.edit.formation.component.QuarterSelectionDialog
import com.wiseduck.squadbuilder.feature.edit.formation.component.QuarterTag
import com.wiseduck.squadbuilder.feature.edit.formation.component.RefereeInput
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.hilt.android.components.ActivityRetainedComponent

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
        formationGraphicsLayer = formationGraphicLayers,
    )

    if (state.deleteConfirmationState.isDialogVisible) {
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnDeleteFormationConfirm) },
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissDeleteDialog) },
            confirmButtonText = stringResource(R.string.dialog_delete_text_button),
            dismissButtonText = stringResource(R.string.dialog_cancel_text_button),
            title = stringResource(R.string.formation_delete_confirm_dialog_title),
            content = {
                Text(
                    text = stringResource(R.string.formation_delete_confirm_dialog_description),
                    color = Color.White,
                )
            },
        )
    }

    if (state.playerAssignmentState.isDialogVisible) {
        PlayerAssignmentModal(
            availablePlayers = state.availablePlayers,
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissPlayerAssignmentDialog) },
            onAssignPlayer = { state.eventSink(FormationUiEvent.OnAssignPlayer(it)) },
        )
    }

    if (state.isSaveDialogVisible) {
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnSaveDialogConfirm) },
            onDismissRequest = { state.eventSink(FormationUiEvent.OnSaveDialogDismiss) },
            confirmButtonText = stringResource(R.string.dialog_save_text_button),
            dismissButtonText = stringResource(R.string.dialog_cancel_text_button),
            title = stringResource(R.string.formation_save_confirm_dialog_title),
            content = {
                TextField(
                    value = state.currentFormationName,
                    onValueChange = { state.eventSink(FormationUiEvent.OnFormationNameChange(it)) },
                    placeholder = {
                        Text(stringResource(R.string.formation_save_confirm_dialog_placeholder))
                    },
                    maxLines = 1,
                )
            },
        )
    }

    if (state.isResetConfirmDialogVisible) {
        SquadBuilderDialog(
            onConfirmRequest = { state.eventSink(FormationUiEvent.OnConfirmReset) },
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissResetDialog) },
            confirmButtonText = stringResource(R.string.dialog_confirm_text_button),
            dismissButtonText = stringResource(R.string.dialog_cancel_text_button),
            title = stringResource(R.string.formation_reset_confirm_dialog_title),
            content = {
                Text(
                    text = stringResource(R.string.formation_reset_confirm_dialog_description),
                    color = Color.White,
                )
            },
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
            onCancelClick = { state.eventSink(FormationUiEvent.OnDismissPlayerInfoDialog) },
        )
    }

    if (state.isListModalVisible) {
        FormationListModal(
            formationList = state.formationList,
            onDismissRequest = { state.eventSink(FormationUiEvent.OnDismissListModal) },
            onFormationCardClick = { state.eventSink(FormationUiEvent.OnFormationCardClick(it)) },
            onDeleteFormationClick = { state.eventSink(FormationUiEvent.OnDeleteFormationClick(it)) },
        )
    }

    if (state.isQuarterSelectionDialogVisible) {
        QuarterSelectionDialog(
            onConfirm = { quarters ->
                state.eventSink(FormationUiEvent.OnSelectQuartersToShare(quarters))
            },
            onDismiss = { state.eventSink(FormationUiEvent.OnDismissQuarterSelectionDialog) },
        )
    }

    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            FormationHeader(
                modifier = Modifier,
                onBackClick = {
                    state.eventSink(FormationUiEvent.OnBackButtonClick)
                },
                onFormationListClick = {
                    state.eventSink(FormationUiEvent.OnFormationListClick)
                },
            )

            FormationController(
                teamName = state.teamName,
                formationName = state.currentFormationName,
                onFormationResetClick = { state.eventSink(FormationUiEvent.OnFormationResetClick) },
                onFormationShareClick = { state.eventSink(FormationUiEvent.OnFormationShareClick) },
                onFormationSaveClick = { state.eventSink(FormationUiEvent.OnFormationSaveClick) },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(4) {
                    SquadBuilderButton(
                        text = stringResource(
                            R.string.quarter_text_button,
                            it + 1,
                        ),
                        onClick = {
                            state.eventSink(FormationUiEvent.OnQuarterChange(it + 1))
                        },
                        colorStyle = if (state.currentQuarter == it + 1) ButtonColorStyle.STROKE else ButtonColorStyle.TEXT_WHITE,
                        sizeStyle = mediumRoundedButtonStyle,
                    )

                    if (it != 3) {
                        Spacer(modifier = Modifier.width(SquadBuilderTheme.spacing.spacing2))
                    }
                }
            }
            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
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

                        if (state.isCapturing) {
                            QuarterTag(
                                modifier = modifier,
                                quarter = state.currentQuarter,
                            )
                        }

                        if (state.isLoading) {
                            SquadBuilderLoadingIndicator()
                        }

                        RefereeInput(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .align(Alignment.TopEnd)
                                .offset(
                                    x = (-5).dp,
                                    y = (14).dp,
                                ),
                            currentQuarter = state.currentQuarter,
                            currentRefereeName = state.allReferees[state.currentQuarter] ?: "",
                            onRefereeNameChange = {
                                state.eventSink(FormationUiEvent.OnRefereeNameChange(state.currentQuarter, it))
                            },
                        )

                        PlayerPlacementLayer(
                            players = state.players,
                            scaleFactor = scaleFactor,
                            onPlayerDragStart = { state.eventSink(FormationUiEvent.OnPlayerDragStart(it)) },
                            onPlayerDrag = { slotId, deltaX, deltaY ->
                                state.eventSink(FormationUiEvent.OnPlayerDrag(slotId, deltaX, deltaY))
                            },
                            onPlayerDragEnd = { slotId, relativeChipWidth, relativeChipHeight ->
                                state.eventSink(FormationUiEvent.OnPlayerDragEnd(slotId, relativeChipWidth, relativeChipHeight))
                            },
                            onPlayerClick = { state.eventSink(FormationUiEvent.OnPlayerClick(it)) },
                            soccerFieldWidth = this.maxWidth,
                            soccerFieldHeight = this.maxHeight,
                        )
                    },
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 10.dp)
                        .width(24.dp)
                        .fillMaxHeight(0.15f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Neutral500,
                    ),
                    shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
                    color = Neutral900,
                    onClick = {
                        state.eventSink(FormationUiEvent.OnPlayerQuarterStatusClick)
                    },
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_side_tab_open),
                            contentDescription = "Open Sidebar Icon",
                            tint = Green500,
                            modifier = Modifier.scale(0.8f),
                        )
                    }
                }
                if (state.isPlayerQuarterStatusVisible) {
                    PlayerQuarterStatusSideBar(
                        modifier = modifier,
                        onDismissRequest = {
                            state.eventSink(FormationUiEvent.OnPlayerQuarterStatusClick)
                        },
                        playerQuarterStatus = state.playerQuarterStatus,
                    )
                }
            }
        }
    }
}

fun Modifier.captureToGraphicsLayer(graphicsLayer: GraphicsLayer) =
    this.drawWithContent {
        graphicsLayer.record { this@drawWithContent.drawContent() }
        drawLayer(graphicsLayer)
    }

@DevicePreview
@Composable
private fun FormationUiPreView() {
    SquadBuilderTheme {
        val dummyPlayers = createDefaultPlayers()

        FormationUi(
            state = FormationUiState(
                isCapturing = true,
                teamId = 1,
                teamName = "비안코",
                players = dummyPlayers,
                eventSink = {},
            ),
        )
    }
}

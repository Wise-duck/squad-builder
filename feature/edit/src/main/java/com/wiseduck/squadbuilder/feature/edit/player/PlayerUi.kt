package com.wiseduck.squadbuilder.feature.edit.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.largeButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerCard
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerFormCard
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerHeader
import com.wiseduck.squadbuilder.feature.screens.PlayerScreen
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomBar
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomTab
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList

@CircuitInject(PlayerScreen::class, ActivityRetainedComponent::class)
@Composable
fun PlayerUi(
    modifier: Modifier = Modifier,
    state: PlayerUiState
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SquadBuilderBottomBar(
                modifier = modifier,
                currentTab = SquadBuilderBottomTab.HOME,
                onTabSelected = {
                    state.eventSink(PlayerUiEvent.OnTabSelect(it.screen))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            PlayerHeader(
                modifier = modifier,
                onBackClick = {
                    state.eventSink(PlayerUiEvent.OnBackButtonClick)
                }
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )
            PlayerContent(
                state = state
            )
        }
    }
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = SquadBuilderTheme.spacing.spacing4),
            text = state.teamName,
            style = SquadBuilderTheme.typography.title1Bold,
            color = White
        )
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2)
        )
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = SquadBuilderTheme.spacing.spacing4),
            text = "선수 목록 (${state.players.size})",
            style = SquadBuilderTheme.typography.body1SemiBold,
            color = Neutral300
        )

        if (state.isShowPlayerCreationSection) {
            PlayerFormCard(
                title = "새 선수 생성",
                player = null,
                commitButtonText = "선수 생성",
                onCommitButtonClick = { _, name, position, backNumber ->
                    state.eventSink(
                        PlayerUiEvent.OnTeamPlayerCreationConfirmButtonClick(
                            name = name,
                            position = position,
                            backNumber = backNumber
                        )
                    )
                },
                onCancelButtonClick = {
                    state.eventSink(PlayerUiEvent.OnTeamPlayerCreationCancelButtonClick)
                }
            )
        } else {
            SquadBuilderButton(
                text = "선수 추가",
                onClick = { state.eventSink(PlayerUiEvent.OnTeamPlayerCreationButtonClick) },
                colorStyle = ButtonColorStyle.TEXT,
                sizeStyle = largeButtonStyle
            )
        }
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2)
        )

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        } else {
            PlayerList(
                state = state,
                onPlayerDeleteClick = {
                    state.eventSink(PlayerUiEvent.OnTeamPlayerDeleteButtonClick(it))
                },
                onPlayerEditClick = {
                    state.eventSink(PlayerUiEvent.OnTeamPlayerEditButtonClick(it))
                }
            )
            if (state.errorMessage != null) {
                SquadBuilderDialog(
                    title = "오류 발생",
                    description = state.errorMessage,
                    onConfirmRequest = {
                        state.eventSink(PlayerUiEvent.OnDialogCloseButtonClick)
                    },
                    confirmButtonText = "확인"
                )
            }
        }
    }
}

@Composable
private fun PlayerList(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
    onPlayerDeleteClick: (Int) -> Unit,
    onPlayerEditClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = state.players,
            key = { player -> player.id }
        ) { player ->
            val isCurrentlyEditing = state.currentEditingPlayerId == player.id
            PlayerCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                player = player,
                onDeleteClick = { onPlayerDeleteClick(player.id) },
                onEditClick = { onPlayerEditClick(player.id) }
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2)
            )
            if (isCurrentlyEditing) {
                PlayerFormCard(
                    title = "${player.name} 선수 정보 수정",
                    player = player,
                    commitButtonText = "수정 완료",
                    onCommitButtonClick = { playerId, name, position, backNumber ->
                        state.eventSink(
                            PlayerUiEvent.OnPlayerUpdateConfirm(
                                playerId = playerId!!,
                                name = name,
                                position = position,
                                backNumber = backNumber,
                            )
                        )
                    },
                    onCancelButtonClick = {
                        state.eventSink(PlayerUiEvent.OnPlayerUpdateCancel)
                    }
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun PlayerUiPreview() {
    val mockPlayers = listOf(
        TeamPlayerModel(
            id = 1,
            teamId = 1,
            name = "선수 1",
            backNumber = 1,
            position = "MD"
        ),
        TeamPlayerModel(
            id = 2,
            teamId = 3,
            name = "잉",
            backNumber = 3,
            position = "FD"
        ),
    )

    SquadBuilderTheme {
        PlayerUi(
            state = PlayerUiState(
                players = mockPlayers.toImmutableList(),
                teamName = "서울 FC 개발팀",
                eventSink = {},
            )
        )
    }
}
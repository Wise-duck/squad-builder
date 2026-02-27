package com.wiseduck.squadbuilder.feature.edit.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.mediumRoundedButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue500
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerFormCard
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerHeader
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerList
import com.wiseduck.squadbuilder.feature.edit.player.mock.fakePlayerUiStateMock
import com.wiseduck.squadbuilder.feature.screens.PlayerScreen
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomBar
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomTab
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(PlayerScreen::class, ActivityRetainedComponent::class)
@Composable
fun PlayerUi(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SquadBuilderBottomBar(
                modifier = modifier,
                currentTab = SquadBuilderBottomTab.HOME,
                onTabSelected = {
                    state.eventSink(PlayerUiEvent.OnTabSelect(it.screen))
                },
            )
        },
    ) { innerPadding ->
        PlayerContent(
            innerPadding = innerPadding,
            state = state,
        )
    }
}

@Composable
private fun PlayerContent(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    state: PlayerUiState,
) {
    Column(
        modifier = modifier.padding(innerPadding),
    ) {
        PlayerHeader(
            onBackClick = {
                state.eventSink(PlayerUiEvent.OnBackButtonClick)
            },
        )
        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = SquadBuilderTheme.spacing.spacing4),
            text = state.teamName,
            style = SquadBuilderTheme.typography.title1Bold,
            color = White,
        )
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4))
            Icon(
                painter = painterResource(R.drawable.ic_group),
                contentDescription = "Group Icon",
                tint = Green500,
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = SquadBuilderTheme.spacing.spacing1),
                text = stringResource(
                    id = R.string.player_list_size_label,
                    formatArgs = arrayOf(state.players.size),
                ),
                style = SquadBuilderTheme.typography.body1SemiBold,
                color = Neutral300,
            )
        }

        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))

        if (state.isShowPlayerCreationSection) {
            PlayerFormCard(
                title = stringResource(R.string.create_player_form_card_title),
                player = null,
                commitButtonText = stringResource(R.string.player_form_card_register_button),
                onCommitButtonClick = { _, name, position, backNumber ->
                    state.eventSink(
                        PlayerUiEvent.OnTeamPlayerCreationConfirmButtonClick(
                            name = name,
                            position = position,
                            backNumber = backNumber,
                        ),
                    )
                },
                onCancelButtonClick = {
                    state.eventSink(PlayerUiEvent.OnTeamPlayerCreationCancelButtonClick)
                },
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Spacer(
                    modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4),
                )
                SquadBuilderButton(
                    text = stringResource(R.string.player_add_text_button),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(com.wiseduck.squadbuilder.core.designsystem.R.drawable.ic_add),
                            contentDescription = "Add Icon",
                            tint = Blue500,
                        )
                    },
                    onClick = { state.eventSink(PlayerUiEvent.OnTeamPlayerCreationButtonClick) },
                    colorStyle = ButtonColorStyle.TEXT_WHITE,
                    sizeStyle = mediumRoundedButtonStyle,
                )
            }
        }
        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))
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
                },
            )
            if (state.errorMessage != null) {
                SquadBuilderDialog(
                    title = stringResource(R.string.load_failed_team_list_dialog_title),
                    description = state.errorMessage,
                    onConfirmRequest = {
                        state.eventSink(PlayerUiEvent.OnDialogCloseButtonClick)
                    },
                    confirmButtonText = stringResource(R.string.dialog_confirm_text_button),
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun PlayerUiPreview() {
    SquadBuilderTheme {
        PlayerUi(
            state = fakePlayerUiStateMock,
        )
    }
}

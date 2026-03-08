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
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.AdBanner
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerFormCard
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerHeader
import com.wiseduck.squadbuilder.feature.edit.player.component.PlayerList
import com.wiseduck.squadbuilder.feature.edit.player.mock.fakePlayerUiStateMock
import com.wiseduck.squadbuilder.feature.screens.PlayerScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(PlayerScreen::class, ActivityRetainedComponent::class)
@Composable
fun PlayerUi(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
) {
    HandlePlayerSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        PlayerContent(
            innerPadding = innerPadding,
            state = state,
        )

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        }
    }
}

@Composable
private fun PlayerContent(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    state: PlayerUiState,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        PlayerHeader(
            onBackClick = {
                state.eventSink(PlayerUiEvent.OnBackClick)
            },
            onAddClick = {
                state.eventSink(PlayerUiEvent.OnPlayerCreationClick)
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
        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))

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
                        PlayerUiEvent.OnPlayerCreationConfirmClick(
                            name = name,
                            position = position,
                            backNumber = backNumber,
                        ),
                    )
                },
                onCancelButtonClick = {
                    state.eventSink(PlayerUiEvent.OnPlayerCreationCancelClick)
                },
            )
        }
        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))

        PlayerList(
            modifier = Modifier.weight(1f),
            state = state,
            players = state.players,
            currentEditingPlayerId = state.currentEditingPlayerId,
            onPlayerDeleteClick = {
                state.eventSink(PlayerUiEvent.OnPlayerDeleteClick(it))
            },
            onPlayerEditClick = {
                state.eventSink(PlayerUiEvent.OnPlayerEditClick(it))
            },
        )

        AdBanner(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = SquadBuilderTheme.spacing.spacing4,
                    horizontal = SquadBuilderTheme.spacing.spacing4,
                ),
            adUnitId = state.admobBannerId,
        )
        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))
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

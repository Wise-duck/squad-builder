package com.wiseduck.squadbuilder.feature.detail.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.AdBanner
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.detail.R
import com.wiseduck.squadbuilder.feature.detail.team.component.MenuButton
import com.wiseduck.squadbuilder.feature.detail.team.component.TeamDetailHeader
import com.wiseduck.squadbuilder.feature.detail.team.mock.teamDetailUiStateMock
import com.wiseduck.squadbuilder.feature.screens.TeamDetailScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(TeamDetailScreen::class, ActivityRetainedComponent::class)
@Composable
fun TeamDetailUi(
    modifier: Modifier = Modifier,
    state: TeamDetailUiState,
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        TeamDetailContent(
            state = state,
            innerPadding = innerPadding,
            onManagePlayersClick = {
                state.eventSink(TeamDetailEvent.OnManagePlayersClick)
            },
            onManageFormationClick = {
                state.eventSink(TeamDetailEvent.OnManageFormationClick)
            },
        )

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        }
    }
}

@Composable
private fun TeamDetailContent(
    state: TeamDetailUiState,
    innerPadding: PaddingValues,
    onManagePlayersClick: () -> Unit,
    onManageFormationClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        TeamDetailHeader(
            onBackClick = {
                state.eventSink(TeamDetailEvent.OnBackButtonClick)
            },
        )
        Column(
            modifier = Modifier
                .padding(SquadBuilderTheme.spacing.spacing4),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            state.team?.let { team ->
                Text(
                    text = team.name,
                    style = SquadBuilderTheme.typography.title1Bold,
                    color = SquadBuilderTheme.colors.basePrimary,
                )

                Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing8))

                MenuButton(
                    icon = painterResource(id = R.drawable.ic_player),
                    title = stringResource(R.string.card_title_manage_players),
                    description = stringResource(R.string.card_desc_manage_players),
                    onClick = onManagePlayersClick,
                )

                Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing8))

                MenuButton(
                    icon = painterResource(id = R.drawable.ic_formation),
                    title = stringResource(R.string.card_title_manage_formation),
                    description = stringResource(R.string.card_desc_manage_formation),
                    onClick = onManageFormationClick,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AdBanner(
                modifier = Modifier.fillMaxWidth(),
                adUnitId = state.admobBannerId,
            )
        }
    }
}

@DevicePreview
@Composable
private fun TeamDetailUiPreview() {
    SquadBuilderTheme {
        TeamDetailUi(
            state = teamDetailUiStateMock,
        )
    }
}

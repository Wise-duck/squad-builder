package com.wiseduck.squadbuilder.feature.detail.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.detail.R
import com.wiseduck.squadbuilder.feature.detail.team.component.MenuButton
import com.wiseduck.squadbuilder.feature.detail.team.component.TeamDetailHeader
import com.wiseduck.squadbuilder.feature.screens.TeamDetailScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(TeamDetailScreen::class, ActivityRetainedComponent::class)
@Composable
fun TeamDetailUi(
    modifier: Modifier = Modifier,
    state: TeamDetailUiState
) {
    SquadBuilderScaffold (
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column (
            modifier = modifier.padding(innerPadding)
        ) {
            TeamDetailHeader(
                modifier = modifier,
                onBackClick = {
                    state.eventSink(TeamDetailEvent.OnBackButtonClick)
                }
            )
            TeamDetailContent(
                state = state,
                OnManagePlayersClick = {
                    state.eventSink(TeamDetailEvent.OnManagePlayersClick)
                },
                OnManageFormationClick = {
                    state.eventSink(TeamDetailEvent.OnManageFormationClick)
                },
            )
        }
    }
}

@Composable
private fun TeamDetailContent(
    modifier: Modifier = Modifier,
    state: TeamDetailUiState,
    OnManagePlayersClick: () -> Unit,
    OnManageFormationClick: () -> Unit,
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(SquadBuilderTheme.spacing.spacing8),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        state.team?.let { team ->
            Text(
                text = team.name,
                style = SquadBuilderTheme.typography.heading1SemiBold,
                color = SquadBuilderTheme.colors.basePrimary // 색상 지정
            )

            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing8))

            MenuButton(
                icon = painterResource(id = R.drawable.ic_player),
                title = stringResource(R.string.card_title_manage_players),
                description = stringResource(R.string.card_desc_manage_players),
                onClick = OnManagePlayersClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            MenuButton(
                icon = painterResource(id = R.drawable.ic_formation),
                title = stringResource(R.string.card_title_manage_formation),
                description = stringResource(R.string.card_desc_manage_formation),
                onClick = OnManageFormationClick
            )
        }

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        }
    }
}


@DevicePreview
@Composable
private fun TeamDetailUi() {
    val previewTeam = TeamModel(
        teamId = 1,
        name = "미리보기용 팀 이름",
        ownerId = "owner",
        ownerEmail = "email",
        createdAt = ""
    )

    SquadBuilderTheme {
        TeamDetailUi(
            state = TeamDetailUiState(
                isLoading = false,
                team = previewTeam,
                eventSink = {}
            )
        )
    }
}

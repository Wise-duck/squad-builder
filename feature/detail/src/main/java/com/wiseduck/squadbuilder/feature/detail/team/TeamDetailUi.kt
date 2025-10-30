package com.wiseduck.squadbuilder.feature.detail.team

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.wiseduck.squadbuilder.feature.detail.R
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
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
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                TeamDetailContent(
                    state = state,
                    OnManagePlayersClick = { },
                    OnManageFormationClick = { },
                )
            }
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
        .padding(horizontal = 32.dp)
    ) {
        state.team?.let { team ->
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = team.name,
                style = SquadBuilderTheme.typography.body1Bold,
                color = SquadBuilderTheme.colors.basePrimary // 색상 지정
            )

            Spacer(modifier = Modifier.height(48.dp))

            MenuButton(
                icon = painterResource(id = R.drawable.ic_player),
                title = "팀 선수 관리",
                description = "선수를 추가, 수정, 삭제합니다.",
                onClick = OnManagePlayersClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            MenuButton(
                icon = painterResource(id = R.drawable.ic_formation),
                title = "팀 포지션 관리",
                description = "전술 및 기본 포메이션을 설정합니다.",
                onClick = OnManageFormationClick
            )
        }
//        Text(
//            text = "팀 정보를 불러올 수 없습니다.",
//            color = SquadBuilderTheme.colors.basePrimary
//        )
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

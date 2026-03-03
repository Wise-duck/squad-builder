package com.wiseduck.squadbuilder.feature.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.home.HomeUiState
import com.wiseduck.squadbuilder.feature.home.R
import com.wiseduck.squadbuilder.feature.home.mock.homeUiStateMock

@Composable
fun TeamList(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onRefresh: () -> Unit,
    onTeamClick: (Int, String) -> Unit,
    onTeamDeleteClick: (Int) -> Unit,
) {
    PullToRefreshBox(
        modifier = modifier,
        state = rememberPullToRefreshState(),
        onRefresh = onRefresh,
        isRefreshing = state.isRefreshing,
    ) {
        if (state.teams.isEmpty()) {
            TeamListEmptyMessage()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    items = state.teams,
                    key = { team -> team.teamId },
                ) { team ->
                    TeamCard(
                        modifier = Modifier.padding(
                            horizontal = SquadBuilderTheme.spacing.spacing4,
                            vertical = SquadBuilderTheme.spacing.spacing3,
                        ),
                        team = team,
                        onDeleteClick = onTeamDeleteClick,
                        onClick = onTeamClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamListEmptyMessage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.logged_in_team_list_empty),
            style = SquadBuilderTheme.typography.body1Regular,
            color = Neutral500,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(SquadBuilderTheme.spacing.spacing8),
        )
    }
}

@ComponentPreview
@Composable
private fun TeamListPreview() {
    SquadBuilderTheme {
        TeamList(
            state = homeUiStateMock,
            onRefresh = {},
            onTeamClick = { _, _ -> },
            onTeamDeleteClick = {},
        )
    }
}

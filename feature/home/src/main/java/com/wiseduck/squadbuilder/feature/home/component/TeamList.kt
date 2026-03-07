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
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.feature.home.R
import com.wiseduck.squadbuilder.feature.home.mock.fakeTeams
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TeamList(
    modifier: Modifier = Modifier,
    teams: ImmutableList<TeamModel>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onTeamClick: (Int, String) -> Unit,
    onTeamDeleteClick: (TeamModel) -> Unit,
) {
    PullToRefreshBox(
        modifier = modifier,
        state = rememberPullToRefreshState(),
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
    ) {
        if (teams.isEmpty()) {
            TeamListEmptyMessage()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    items = teams,
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
            teams = fakeTeams.toImmutableList(),
            isRefreshing = false,
            onRefresh = {},
            onTeamClick = { _, _ -> },
            onTeamDeleteClick = {},
        )
    }
}

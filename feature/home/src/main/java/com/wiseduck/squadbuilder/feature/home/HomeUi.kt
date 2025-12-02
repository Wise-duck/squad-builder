package com.wiseduck.squadbuilder.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.mediumButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.AdBanner
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.home.component.HomeHeader
import com.wiseduck.squadbuilder.feature.home.component.TeamCard
import com.wiseduck.squadbuilder.feature.home.component.TeamCreateSection
import com.wiseduck.squadbuilder.feature.home.component.TeamSortDropdown
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomBar
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomTab
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    state: HomeUiState,
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SquadBuilderBottomBar(
                modifier = modifier,
                currentTab = SquadBuilderBottomTab.HOME,
                onTabSelected = {
                    state.eventSink(HomeUiEvent.OnTabSelect(it.screen))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            HomeHeader(
                modifier = modifier,
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4),
            )
            HomeContent(
                modifier = Modifier
                    .weight(1f),
                state = state,
                onTeamCreateClick = {
                    state.eventSink(HomeUiEvent.OnTeamCreateButtonClick(it))
                },
                onTeamClick = { teamId, teamName ->
                    state.eventSink(HomeUiEvent.OnTeamCardClick(teamId, teamName))
                },
                onTeamDeleteClick = {
                    state.eventSink(HomeUiEvent.OnTeamDeleteButtonClick(it))
                },
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onTeamCreateClick: (String) -> Unit,
    onTeamClick: (Int, String) -> Unit,
    onTeamDeleteClick: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        if (state.isLoggedIn) {
            TeamCreateSection(
                modifier = Modifier.fillMaxWidth(),
                onTeamCreateClick = onTeamCreateClick,
            )

            TeamSortDropdown(
                modifier = Modifier
                    .padding(
                        horizontal = SquadBuilderTheme.spacing.spacing2,
                    ),
                currentSortOption = state.currentSortOption,
                onSortOptionSelected = {
                    state.eventSink(HomeUiEvent.OnSortOptionSelect(it))
                },
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2),
            )
        }

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        } else if (state.teams.isEmpty()) {
            HomeEmptyContent(
                modifier = Modifier.weight(1f),
                state = state,
                onLoginClick = {
                    onTeamCreateClick("")
                },
            )
        } else {
            TeamList(
                modifier = Modifier.weight(1f),
                state = state,
                onTeamClick = onTeamClick,
                onTeamDeleteClick = onTeamDeleteClick,
                onRefresh = {
                    state.eventSink(HomeUiEvent.OnRefresh)
                },
            )
            if (state.errorMessage != null) {
                SquadBuilderDialog(
                    title = stringResource(R.string.load_failed_team_list_dialog_title),
                    description = state.errorMessage,
                    onConfirmRequest = {
                        state.eventSink(HomeUiEvent.OnDialogCloseButtonClick)
                    },
                    confirmButtonText = stringResource(R.string.dialog_confirm_text_button),
                )
            }
        }

        AdBanner(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = SquadBuilderTheme.spacing.spacing4,
                    horizontal = SquadBuilderTheme.spacing.spacing4,
                ),
            adUnitId = state.adUnitId,
        )

        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2),
        )
    }
}

@Composable
private fun HomeEmptyContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onLoginClick: () -> Unit,
) {
    if (state.isLoggedIn) {
        LoggedInTeamListEmptyMessage(modifier = modifier)
    } else {
        GuestInfoMessage(
            modifier = modifier,
            onLoginClick = onLoginClick,
        )
    }
}

@Composable
private fun LoggedInTeamListEmptyMessage(
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

@Composable
private fun GuestInfoMessage(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_lock),
            contentDescription = "Lock Icon",
            tint = Neutral800,
            modifier = Modifier.size(300.dp),
        )
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.guest_mode_team_list_empty),
                style = SquadBuilderTheme.typography.body1Regular,
                color = Green500,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(SquadBuilderTheme.spacing.spacing6),
            )
            SquadBuilderButton(
                text = stringResource(R.string.login_text_button),
                onClick = onLoginClick,
                sizeStyle = mediumButtonStyle,
                colorStyle = ButtonColorStyle.TEXT,
            )
        }
    }
}

@Composable
private fun TeamList(
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
        LazyColumn(
            modifier = modifier.fillMaxSize(),
        ) {
            items(
                items = state.teams,
                key = { team -> team.teamId },
            ) { team ->
                TeamCard(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    team = team,
                    onDeleteClick = onTeamDeleteClick,
                    onClick = onTeamClick,
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun HomeUi() {
//    val sampleTeams =
//        listOf(
//            TeamModel(
//                teamId = 1,
//                name = "비얀코",
//                ownerId = "owner1",
//                ownerEmail = "owner1@example.com",
//                createdAt = "2025-10-29T17:30:00.000Z",
//            ),
//            TeamModel(
//                teamId = 2,
//                name = "빠삐코",
//                ownerId = "owner2",
//                ownerEmail = "owner2@example.com",
//                createdAt = "2025-10-29T17:31:00.000Z",
//            ),
//        )

    SquadBuilderTheme {
        HomeUi(
            state = HomeUiState(
                teams = emptyList<TeamModel>().toImmutableList(),
                isLoggedIn = false,
                eventSink = {},
            ),
        )
//        HomeUi(
//            state = HomeUiState(
// //                teams = sampleTeams.toImmutableList(),
//                teams = emptyList<TeamModel>().toImmutableList(),
//                isLoggedIn = true,
//                eventSink = {},
//            ),
//        )
    }
}

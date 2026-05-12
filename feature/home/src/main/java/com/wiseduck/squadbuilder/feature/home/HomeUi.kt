package com.wiseduck.squadbuilder.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.Team
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.AdBanner
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.home.component.GuestModeHomeUiContent
import com.wiseduck.squadbuilder.feature.home.component.HomeHeader
import com.wiseduck.squadbuilder.feature.home.component.TeamCreateSection
import com.wiseduck.squadbuilder.feature.home.component.TeamList
import com.wiseduck.squadbuilder.feature.home.component.TeamSortDropdown
import com.wiseduck.squadbuilder.feature.home.mock.fakeTeam
import com.wiseduck.squadbuilder.feature.home.mock.homeUiStateMock
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomBar
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomTab
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    state: HomeUiState,
) {
    HandleHomeSideEffect(
        sideEffect = state.sideEffect,
        eventSink = state.eventSink,
    )

    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SquadBuilderBottomBar(
                tabs = SquadBuilderBottomTab.entries.toImmutableList(),
                currentTab = SquadBuilderBottomTab.HOME,
                onTabSelected = {
                    state.eventSink(HomeUiEvent.OnTabSelect(it.screen))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            HomeHeader()
            HomeContent(
                isLoggedIn = state.isLoggedIn,
                isLoading = state.isLoading,
                isRefreshing = state.isRefreshing,
                teams = state.teams,
                currentSortOption = state.currentSortOption,
                adUnitId = state.adUnitId,
                onEvent = state.eventSink,
                onTeamCreateClick = { teamName ->
                    state.eventSink(HomeUiEvent.OnTeamCreateButtonClick(teamName))
                },
            )
        }

        state.teamToDelete?.let {
            SquadBuilderDialog(
                onDismissRequest = { state.eventSink(HomeUiEvent.OnDismissTeamDeleteDialog) },
                onConfirmRequest = {
                    state.eventSink(HomeUiEvent.OnTeamDeleteConfirm(state.teamToDelete.teamId))
                },
                dismissButtonText = stringResource(R.string.cancel_button_label),
                confirmButtonText = stringResource(R.string.confirm_button_label),
                title = stringResource(R.string.delete_confirm_dialog_title),
                content = {
                    Text(
                        text = stringResource(
                            R.string.delete_confirm_dialog_description,
                            state.teamToDelete.name,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
    isLoading: Boolean,
    isRefreshing: Boolean,
    teams: ImmutableList<Team>,
    currentSortOption: TeamSortOption,
    adUnitId: String,
    onEvent: (HomeUiEvent) -> Unit,
    onTeamCreateClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (!isLoggedIn) {
            GuestModeHomeUiContent(
                adUnitId = adUnitId,
                onLoginClick = {
                    onTeamCreateClick("")
                },
            )
        } else {
            Column(modifier = modifier.fillMaxSize()) {
                TeamCreateSection(
                    onTeamCreateClick = onTeamCreateClick,
                )

                Row(modifier = Modifier.fillMaxWidth(0.5f)) {
                    TeamSortDropdown(
                        currentSortOption = currentSortOption,
                        onSortOptionSelected = {
                            onEvent(HomeUiEvent.OnSortOptionSelect(it))
                        },
                    )
                }

                TeamList(
                    modifier = Modifier.weight(1f),
                    teams = teams,
                    isRefreshing = isRefreshing,
                    onTeamClick = { teamId, teamName ->
                        onEvent(HomeUiEvent.OnTeamCardClick(teamId, teamName))
                    },
                    onTeamDeleteClick = { team ->
                        onEvent(HomeUiEvent.OnTeamDeleteClick(team))
                    },
                    onRefresh = {
                        onEvent(HomeUiEvent.OnRefresh)
                    },
                )

                AdBanner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = SquadBuilderTheme.spacing.spacing4,
                            horizontal = SquadBuilderTheme.spacing.spacing4,
                        ),
                    adUnitId = adUnitId,
                )
                Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))

                if (isLoading) {
                    SquadBuilderLoadingIndicator()
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun HomeUiPreview() {
    SquadBuilderTheme {
        HomeUi(
            state = homeUiStateMock,
        )
    }
}

@DevicePreview
@Composable
private fun HomeUiDeleteDialogPreview() {
    SquadBuilderTheme {
        HomeUi(
            state = homeUiStateMock.copy(
                teamToDelete = fakeTeam,
            ),
        )
    }
}

@DevicePreview
@Composable
private fun HomeUiEmptyPreview() {
    SquadBuilderTheme {
        HomeUi(
            state = homeUiStateMock.copy(
                teams = emptyList<Team>().toImmutableList(),
            ),
        )
    }
}

@DevicePreview
@Composable
private fun HomeUiGuestPreview() {
    SquadBuilderTheme {
        HomeUi(
            state = homeUiStateMock.copy(
                isLoggedIn = false,
                teams = emptyList<Team>().toImmutableList(),
            ),
        )
    }
}

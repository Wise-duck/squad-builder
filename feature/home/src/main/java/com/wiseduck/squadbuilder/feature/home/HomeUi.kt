package com.wiseduck.squadbuilder.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.AdBanner
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.home.component.GuestModeHomeUiContent
import com.wiseduck.squadbuilder.feature.home.component.HomeHeader
import com.wiseduck.squadbuilder.feature.home.component.TeamCreateSection
import com.wiseduck.squadbuilder.feature.home.component.TeamList
import com.wiseduck.squadbuilder.feature.home.component.TeamSortDropdown
import com.wiseduck.squadbuilder.feature.home.mock.homeUiStateMock
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
    HandleHomeSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

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
        HomeContent(
            state = state,
            innerPadding = innerPadding,
            onTeamCreateClick = {
                state.eventSink(HomeUiEvent.OnTeamCreateButtonClick(it))
            },
        )

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    innerPadding: PaddingValues,
    onTeamCreateClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        HomeHeader()

        if (!state.isLoggedIn) {
            GuestModeHomeUiContent(
                state = state,
                onLoginClick = {
                    onTeamCreateClick("")
                },
            )
        } else {
            TeamCreateSection(
                onTeamCreateClick = onTeamCreateClick,
            )

            Row(modifier = Modifier.fillMaxWidth(0.5f)) {
                TeamSortDropdown(
                    currentSortOption = state.currentSortOption,
                    onSortOptionSelected = {
                        state.eventSink(HomeUiEvent.OnSortOptionSelect(it))
                    },
                )
            }

            TeamList(
                modifier = Modifier.weight(1f),
                state = state,
                onTeamClick = { teamId, teamName ->
                    state.eventSink(HomeUiEvent.OnTeamCardClick(teamId, teamName))
                },
                onTeamDeleteClick = {
                    state.eventSink(HomeUiEvent.OnTeamDeleteButtonClick(it))
                },
                onRefresh = {
                    state.eventSink(HomeUiEvent.OnRefresh)
                },
            )

            AdBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = SquadBuilderTheme.spacing.spacing4,
                        horizontal = SquadBuilderTheme.spacing.spacing4,
                    ),
                adUnitId = state.adUnitId,
            )
            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))
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
private fun HomeUiEmptyPreview() {
    SquadBuilderTheme {
        HomeUi(
            state = homeUiStateMock.copy(
                teams = emptyList<TeamModel>().toImmutableList(),
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
                teams = emptyList<TeamModel>().toImmutableList(),
            ),
        )
    }
}

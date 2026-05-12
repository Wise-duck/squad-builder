package com.wiseduck.squadbuilder.feature.detail.team.mock

import com.wiseduck.squadbuilder.core.model.Team
import com.wiseduck.squadbuilder.feature.detail.team.TeamDetailUiState

internal val fakeTeam = Team(
    teamId = 1,
    name = "Team 1",
    ownerId = "owner",
    ownerEmail = "email",
    createdAt = "",
)

internal val teamDetailUiStateMock = TeamDetailUiState(
    isLoading = false,
    team = fakeTeam,
    admobBannerId = "",
    eventSink = {},
)

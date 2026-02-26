package com.wiseduck.squadbuilder.feature.detail.team.mock

import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.feature.detail.team.TeamDetailUiState

internal val fakeTeam = TeamModel(
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

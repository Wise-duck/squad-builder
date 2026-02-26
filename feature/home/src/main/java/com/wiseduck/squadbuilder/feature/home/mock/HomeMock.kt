package com.wiseduck.squadbuilder.feature.home.mock

import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.feature.home.HomeUiState
import kotlinx.collections.immutable.toImmutableList

internal val fakeTeams = listOf(
    TeamModel(
        teamId = 1,
        name = "팀1",
        ownerId = "owner1",
        ownerEmail = "owner1@example.com",
        createdAt = "2025-10-29T17:30:00.000Z",
    ),
    TeamModel(
        teamId = 2,
        name = "팀2",
        ownerId = "owner2",
        ownerEmail = "owner2@example.com",
        createdAt = "2025-10-29T17:31:00.000Z",
    ),
)

internal val homeUiStateMock = HomeUiState(
    teams = fakeTeams.toImmutableList(),
    isLoggedIn = true,
    eventSink = {},
)

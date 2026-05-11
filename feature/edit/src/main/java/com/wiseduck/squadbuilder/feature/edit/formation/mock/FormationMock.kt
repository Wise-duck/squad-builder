package com.wiseduck.squadbuilder.feature.edit.formation.mock

import com.wiseduck.squadbuilder.core.model.FormationListItem
import com.wiseduck.squadbuilder.core.model.PlayerQuarterStatus
import com.wiseduck.squadbuilder.core.model.TeamPlayer
import com.wiseduck.squadbuilder.feature.edit.formation.FormationUiState
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers

val fakeTeamPlayers = listOf(
    TeamPlayer(teamId = 1, id = 1, name = "손흥민", backNumber = 7, position = "FW"),
    TeamPlayer(teamId = 1, id = 2, name = "김민재", backNumber = 3, position = "DF"),
    TeamPlayer(teamId = 1, id = 3, name = "이강인", backNumber = 18, position = "MF"),
    TeamPlayer(teamId = 1, id = 4, name = "조현우", backNumber = 1, position = "GK"),
)

val fakeFormationListItem = FormationListItem(1, "4-3-3", "2026-01-01T00:00:00Z")

val fakeFormationList = listOf(
    FormationListItem(1, "4-3-3", "2026-01-01T00:00:00Z"),
    FormationListItem(2, "4-4-2", "2026-01-02T00:00:00Z"),
    FormationListItem(3, "3-5-2", "2026-01-03T00:00:00Z"),
)

val dummyPlayers = createDefaultPlayers()

val dummyPlayerQuarterStatus = listOf(
    PlayerQuarterStatus(
        playerId = 1,
        playerName = "손흥민",
        quarters = listOf(1, 2, 4),
        backNumber = 7,
        position = "FW",
    ),
    PlayerQuarterStatus(
        playerId = 2,
        playerName = "이강인",
        quarters = listOf(1, 3),
        backNumber = 10,
        position = "MF",
    ),
    PlayerQuarterStatus(
        playerId = 3,
        playerName = "김민재",
        quarters = listOf(2, 3, 4),
        backNumber = 4,
        position = "DF",
    ),
)

internal val formationUiStateMock = FormationUiState(
    isCapturing = true,
    teamId = 1,
    teamName = "Team 1",
    players = dummyPlayers,
    eventSink = {},
)

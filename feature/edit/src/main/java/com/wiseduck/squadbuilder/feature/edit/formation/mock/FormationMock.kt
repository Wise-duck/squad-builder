package com.wiseduck.squadbuilder.feature.edit.formation.mock

import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.PlayerQuarterStatusModel
import com.wiseduck.squadbuilder.feature.edit.formation.FormationUiState
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers

val fakeFormationListItem =  FormationListItemModel(1, "4-3-3", "2026-01-01T00:00:00Z")

val fakeFormationList = listOf(
    FormationListItemModel(1, "4-3-3", "2026-01-01T00:00:00Z"),
    FormationListItemModel(2, "4-4-2", "2026-01-02T00:00:00Z"),
    FormationListItemModel(3, "3-5-2", "2026-01-03T00:00:00Z"),
)

val dummyPlayers = createDefaultPlayers()

val dummyPlayerQuarterStatus = listOf(
    PlayerQuarterStatusModel(
        playerId = 1,
        playerName = "손흥민",
        quarters = listOf(1, 2, 4),
        backNumber = 7,
        position = "FW",
    ),
    PlayerQuarterStatusModel(
        playerId = 2,
        playerName = "이강인",
        quarters = listOf(1, 3),
        backNumber = 10,
        position = "MF",
    ),
    PlayerQuarterStatusModel(
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

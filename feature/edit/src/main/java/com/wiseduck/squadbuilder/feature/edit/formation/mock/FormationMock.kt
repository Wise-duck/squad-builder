package com.wiseduck.squadbuilder.feature.edit.formation.mock

import com.wiseduck.squadbuilder.feature.edit.formation.FormationUiState
import com.wiseduck.squadbuilder.feature.edit.formation.data.createDefaultPlayers

val dummyPlayers = createDefaultPlayers()

internal val formationUiStateMock = FormationUiState(
    isCapturing = true,
    teamId = 1,
    teamName = "Team 1",
    players = dummyPlayers,
    eventSink = {},
)

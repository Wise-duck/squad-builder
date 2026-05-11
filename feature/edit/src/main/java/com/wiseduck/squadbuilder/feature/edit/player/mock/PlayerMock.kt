package com.wiseduck.squadbuilder.feature.edit.player.mock

import com.wiseduck.squadbuilder.core.model.TeamPlayer
import kotlinx.collections.immutable.toImmutableList

internal val fakePlayer = TeamPlayer(
    id = 1,
    teamId = 1,
    name = "선수 1",
    backNumber = 1,
    position = "MD",
)

internal val fakePlayers = listOf(
    TeamPlayer(
        id = 1,
        teamId = 1,
        name = "선수 1",
        backNumber = 1,
        position = "MD",
    ),
    TeamPlayer(
        id = 2,
        teamId = 3,
        name = "잉",
        backNumber = 3,
        position = "FD",
    ),
)

internal val fakePlayerUiStateMock =
    _root_ide_package_.com.wiseduck.squadbuilder.feature.edit.player.PlayerUiState(
        admobBannerId = "",
        players = fakePlayers.toImmutableList(),
        teamName = "서울 FC 개발팀",
        eventSink = {},
    )

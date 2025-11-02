package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.TeamPlayerModel

interface PlayerRepository {
    suspend fun updateTeamPlayer(
        teamId: Int,
        playerId: Int,
        name: String,
        position: String,
        backNumber: Int
    ) : Result<TeamPlayerModel>

    suspend fun deleteTeamPlayer(
        teamId: Int,
        playerId: Int
    ) : Result<Unit>

    suspend fun getTeamPlayers(
        teamId: Int
    ) : Result<List<TeamPlayerModel>>

    suspend fun createTeamPlayer(
        teamId: Int,
        name: String,
        position: String,
        backNumber: Int
    ) : Result<TeamPlayerModel>
}
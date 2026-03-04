package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.common.utils.runSuspendCatching
import com.wiseduck.squadbuilder.core.data.api.repository.PlayerRepository
import com.wiseduck.squadbuilder.core.data.impl.mapper.toModel
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.core.network.request.TeamPlayerCreationRequest
import com.wiseduck.squadbuilder.core.network.request.TeamPlayerUpdateRequest
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import jakarta.inject.Inject

internal class PlayerRepositoryImpl @Inject constructor(
    private val service: SquadBuilderService,
) : PlayerRepository {

    override suspend fun updateTeamPlayer(
        teamId: Int,
        playerId: Int,
        name: String,
        position: String,
        backNumber: Int,
    ): Result<TeamPlayerModel> = runSuspendCatching {
        service.updateTeamPlayer(
            teamId = teamId,
            playerId = playerId,
            TeamPlayerUpdateRequest(name, position, backNumber),
        ).toModel()
    }

    override suspend fun deleteTeamPlayer(
        teamId: Int,
        playerId: Int,
    ): Result<Unit> = runSuspendCatching {
        service.deleteTeamPlayer(teamId, playerId)
    }

    override suspend fun getTeamPlayers(
        teamId: Int,
    ): Result<List<TeamPlayerModel>> = runSuspendCatching {
        service.getTeamPlayers(teamId).map { it.toModel() }
    }

    override suspend fun createTeamPlayer(
        teamId: Int,
        name: String,
        position: String,
        backNumber: Int,
    ): Result<TeamPlayerModel> = runSuspendCatching {
        service.createTeamPlayer(
            teamId = teamId,
            TeamPlayerCreationRequest(name, position, backNumber),
        ).toModel()
    }
}

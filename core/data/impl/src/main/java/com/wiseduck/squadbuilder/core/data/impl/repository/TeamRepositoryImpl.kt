package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import javax.inject.Inject
import com.wiseduck.squadbuilder.core.data.impl.mapper.toModel
import com.wiseduck.squadbuilder.core.model.TeamModel

internal class TeamRepositoryImpl @Inject constructor(
    private val service: SquadBuilderService
) : TeamRepository {

    override suspend fun getTeams(): Result<List<TeamModel>> = runCatching {
        service.getTeams().map { it.toModel() }
    }

    override suspend fun deleteTeam(teamId: Int): Result<Unit> = runCatching {
        service.deleteTeam(teamId)
    }
}
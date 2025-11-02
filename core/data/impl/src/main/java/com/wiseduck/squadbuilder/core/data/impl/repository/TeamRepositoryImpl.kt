package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import javax.inject.Inject
import com.wiseduck.squadbuilder.core.data.impl.mapper.toModel
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.core.network.request.TeamCreationRequest

internal class TeamRepositoryImpl @Inject constructor(
    private val service: SquadBuilderService
) : TeamRepository {
    override suspend fun createTeam(name: String): Result<TeamModel> = runCatching {
        val request = TeamCreationRequest(name)
        service.createTeam(request).toModel()
    }

    override suspend fun getTeams(): Result<List<TeamModel>> = runCatching {
        service.getTeams().map { it.toModel() }
    }

    override suspend fun deleteTeam(teamId: Int): Result<Unit> = runCatching {
        service.deleteTeam(teamId)
    }
}
package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.model.HomeModel
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import javax.inject.Inject
import com.wiseduck.squadbuilder.core.data.impl.mapper.toModel

internal class TeamRepositoryImpl @Inject constructor(
    private val service: SquadBuilderService
) : TeamRepository {

    override suspend fun getTeams(): Result<HomeModel> = runCatching {
        service.getTeams().toModel()
    }
}
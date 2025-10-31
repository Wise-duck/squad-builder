package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.FormationRepository
import com.wiseduck.squadbuilder.core.data.impl.mapper.toModel
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import jakarta.inject.Inject

internal class FormationRepositoryImpl @Inject constructor(
    private val service: SquadBuilderService
) : FormationRepository {
    override suspend fun getFormationList(teamId: Int): Result<List<FormationListItemModel>> = runCatching {
        service.getFormationList(teamId).map { it.toModel() }
    }
}
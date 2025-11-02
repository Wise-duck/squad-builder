package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.FormationRepository
import com.wiseduck.squadbuilder.core.data.impl.mapper.toModel
import com.wiseduck.squadbuilder.core.data.impl.mapper.toRequest
import com.wiseduck.squadbuilder.core.model.FormationDetailModel
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.FormationSaveModel
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import jakarta.inject.Inject

internal class FormationRepositoryImpl @Inject constructor(
    private val service: SquadBuilderService
) : FormationRepository {
    override suspend fun getFormationList(teamId: Int): Result<List<FormationListItemModel>> = runCatching {
        service.getFormationList(teamId).map { it.toModel() }
    }

    override suspend fun getFormationDetail(formationId: Int): Result<FormationDetailModel> = runCatching {
        service.getFormationDetail(formationId).toModel()
    }

    override suspend fun createFormation(request: FormationSaveModel): Result<Unit> = runCatching {
        service.createFormation(request.toRequest())
    }

    override suspend fun updateFormation(formationId: Int, request: FormationSaveModel): Result<Unit> = runCatching {
        service.updateFormation(formationId, request.toRequest())
    }

    override suspend fun deleteFormation(formationId: Int): Result<Unit> = runCatching {
        service.deleteFormation(formationId)
    }
}
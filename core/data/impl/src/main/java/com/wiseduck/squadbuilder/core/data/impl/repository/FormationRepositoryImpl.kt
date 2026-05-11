package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.common.utils.runSuspendCatching
import com.wiseduck.squadbuilder.core.data.api.repository.FormationRepository
import com.wiseduck.squadbuilder.core.data.impl.mapper.toModel
import com.wiseduck.squadbuilder.core.data.impl.mapper.toRequest
import com.wiseduck.squadbuilder.core.model.FormationDetail
import com.wiseduck.squadbuilder.core.model.FormationListItem
import com.wiseduck.squadbuilder.core.model.FormationSaveModel
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import jakarta.inject.Inject

internal class FormationRepositoryImpl @Inject constructor(
    private val service: SquadBuilderService,
) : FormationRepository {

    override suspend fun getFormationList(
        teamId: Int,
    ): Result<List<FormationListItem>> = runSuspendCatching {
        service.getFormationList(teamId).map { it.toModel() }
    }

    override suspend fun getFormationDetail(
        formationId: Int,
    ): Result<FormationDetail> = runSuspendCatching {
        service.getFormationDetail(formationId).toModel()
    }

    override suspend fun createFormation(
        request: FormationSaveModel,
    ): Result<Unit> = runSuspendCatching {
        service.createFormation(request.toRequest())
    }

    override suspend fun updateFormation(
        formationId: Int,
        request: FormationSaveModel,
    ): Result<Unit> = runSuspendCatching {
        service.updateFormation(formationId, request.toRequest())
    }

    override suspend fun deleteFormation(
        formationId: Int,
    ): Result<Unit> = runSuspendCatching {
        service.deleteFormation(formationId)
    }
}

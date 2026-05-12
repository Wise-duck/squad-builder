package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.FormationDetail
import com.wiseduck.squadbuilder.core.model.FormationListItem
import com.wiseduck.squadbuilder.core.model.FormationSaveModel

interface FormationRepository {
    suspend fun getFormationList(teamId: Int): Result<List<FormationListItem>>

    suspend fun getFormationDetail(formationId: Int): Result<FormationDetail>

    suspend fun createFormation(request: FormationSaveModel): Result<Unit>

    suspend fun updateFormation(
        formationId: Int,
        request: FormationSaveModel,
    ): Result<Unit>

    suspend fun deleteFormation(formationId: Int): Result<Unit>
}

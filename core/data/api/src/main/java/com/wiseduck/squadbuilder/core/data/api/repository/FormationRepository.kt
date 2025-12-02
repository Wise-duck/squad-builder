package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.FormationDetailModel
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.FormationSaveModel

interface FormationRepository {
    suspend fun getFormationList(teamId: Int): Result<List<FormationListItemModel>>

    suspend fun getFormationDetail(formationId: Int): Result<FormationDetailModel>

    suspend fun createFormation(request: FormationSaveModel): Result<Unit>

    suspend fun updateFormation(
        formationId: Int,
        request: FormationSaveModel,
    ): Result<Unit>

    suspend fun deleteFormation(formationId: Int): Result<Unit>
}

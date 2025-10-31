package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.FormationListItemModel

interface FormationRepository {

    suspend fun getFormationList(teamId: Int): Result<List<FormationListItemModel>>
}
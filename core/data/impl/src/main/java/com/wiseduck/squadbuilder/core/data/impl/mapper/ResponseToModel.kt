package com.wiseduck.squadbuilder.core.data.impl.mapper

import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.HomeModel
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.core.network.response.FormationListItemResponse
import com.wiseduck.squadbuilder.core.network.response.HomeResponse
import com.wiseduck.squadbuilder.core.network.response.Team
import com.wiseduck.squadbuilder.core.network.response.TeamResponse

internal fun TeamResponse.toModel(): TeamModel {
    return TeamModel(
        teamId = teamId,
        name = name,
        ownerId = ownerId,
        ownerEmail = ownerEmail,
        createdAt = createdAt
    )
}

internal fun HomeResponse.toModel(): HomeModel {
    return HomeModel(
        teams = teams.map { it.toModel() }
    )
}

internal fun Team.toModel(): TeamModel {
    return TeamModel(
        teamId = teamId,
        name = name,
        ownerId = ownerId,
        ownerEmail = ownerEmail,
        createdAt = createdAt
    )
}

internal fun FormationListItemResponse.toModel(): FormationListItemModel {
    return FormationListItemModel(
        formationId = formationId,
        name = name,
        createdAt = createdAt
    )
}
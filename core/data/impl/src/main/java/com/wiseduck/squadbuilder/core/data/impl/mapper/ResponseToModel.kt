package com.wiseduck.squadbuilder.core.data.impl.mapper

import com.wiseduck.squadbuilder.core.model.FormationDetailModel
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.core.model.HomeModel
import com.wiseduck.squadbuilder.core.model.Placement
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.core.network.response.FormationDetailResponse
import com.wiseduck.squadbuilder.core.network.response.FormationListItemResponse
import com.wiseduck.squadbuilder.core.network.response.HomeResponse
import com.wiseduck.squadbuilder.core.network.response.PlacementResponse
import com.wiseduck.squadbuilder.core.network.response.Team
import com.wiseduck.squadbuilder.core.network.response.TeamPlayerResponse
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

internal fun TeamPlayerResponse.toModel() : TeamPlayerModel {
    return TeamPlayerModel(
        id = playerId,
        teamId = teamId,
        name = name,
        backNumber = number,
        position = position
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

internal fun PlacementResponse.toModel(): Placement {
    return Placement(
        slotId = playerId,
        playerId = playerId,
        playerName = playerName,
        playerPosition = playerPosition,
        playerBackNumber = playerBackNumber.toString(),
        coordX = coordX / 1000f,
        coordY = coordY / 1000f
    )
}

internal fun FormationDetailResponse.toModel(): FormationDetailModel {
    return FormationDetailModel(
        formationId = formationId,
        name = name,
        createdAt = createdAt,
        teamId = teamId,
        teamName = teamName,
        placements = placements.map { it.toModel() }
    )
}

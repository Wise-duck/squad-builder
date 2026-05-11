package com.wiseduck.squadbuilder.core.data.impl.mapper

import com.wiseduck.squadbuilder.core.model.FormationDetail
import com.wiseduck.squadbuilder.core.model.FormationListItem
import com.wiseduck.squadbuilder.core.model.Placement
import com.wiseduck.squadbuilder.core.model.Team
import com.wiseduck.squadbuilder.core.model.TeamPlayer
import com.wiseduck.squadbuilder.core.network.response.FormationDetailResponse
import com.wiseduck.squadbuilder.core.network.response.FormationListItemResponse
import com.wiseduck.squadbuilder.core.network.response.PlacementResponse
import com.wiseduck.squadbuilder.core.network.response.TeamPlayerResponse
import com.wiseduck.squadbuilder.core.network.response.TeamResponse

internal fun TeamResponse.toModel(): Team {
    return Team(
        teamId = teamId,
        name = name,
        ownerId = ownerId,
        ownerEmail = ownerEmail,
        createdAt = createdAt,
    )
}

internal fun TeamPlayerResponse.toModel(): TeamPlayer {
    return TeamPlayer(
        id = playerId,
        teamId = teamId,
        name = name,
        backNumber = number,
        position = position,
    )
}

internal fun FormationListItemResponse.toModel(): FormationListItem {
    return FormationListItem(
        formationId = formationId,
        name = name,
        createdAt = createdAt,
    )
}

internal fun PlacementResponse.toModel(): Placement {
    return Placement(
        slotId = playerId,
        playerId = playerId,
        playerName = playerName,
        playerPosition = playerPosition,
        playerBackNumber = playerBackNumber.toString(),
        quarter = quarter,
        coordX = coordX / 1000f,
        coordY = coordY / 1000f,
    )
}

internal fun FormationDetailResponse.toModel(): FormationDetail {
    return FormationDetail(
        formationId = formationId,
        name = name,
        createdAt = createdAt,
        teamId = teamId,
        teamName = teamName,
        placements = placements.map { it.toModel() },
        referees = referees,
    )
}

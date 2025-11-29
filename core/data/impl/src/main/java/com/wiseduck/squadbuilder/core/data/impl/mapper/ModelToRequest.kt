package com.wiseduck.squadbuilder.core.data.impl.mapper

import com.wiseduck.squadbuilder.core.model.FormationSaveModel
import com.wiseduck.squadbuilder.core.model.PlacementSaveModel
import com.wiseduck.squadbuilder.core.network.request.FormationSaveRequest
import com.wiseduck.squadbuilder.core.network.request.PlacementSaveRequest

internal fun PlacementSaveModel.toRequest(): PlacementSaveRequest =
    PlacementSaveRequest(
        playerId = playerId,
        quarter = quarter,
        coordX = coordX,
        coordY = coordY,
    )

internal fun FormationSaveModel.toRequest(): FormationSaveRequest =
    FormationSaveRequest(
        teamId = teamId,
        name = name,
        placements = placements.map { it.toRequest() },
        referees = referees,
    )

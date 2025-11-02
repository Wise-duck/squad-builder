package com.wiseduck.squadbuilder.feature.edit.formation.data

import com.wiseduck.squadbuilder.core.model.Placement
import com.wiseduck.squadbuilder.feature.edit.formation.data.FormationConstants.SLOT_ZONES_BOUNDS

fun createDefaultPlayers(): List<Placement> {
    val formation433 = listOf(
        "GK",
        "LB", "LCB", "RCB", "RB",
        "LCM", "CDM", "RCM",
        "LW", "ST", "RW"
    )

    return formation433.mapIndexed { index, positionKey ->
        val bounds = SLOT_ZONES_BOUNDS[positionKey]
            ?: PositionBounds(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f)

        Placement(
            slotId = index + 1,
            playerId = null,
            playerName = "Player",
            playerPosition = positionKey,
            playerBackNumber = "+",
            coordX = bounds.centerX,
            coordY = bounds.centerY
        )
    }
}

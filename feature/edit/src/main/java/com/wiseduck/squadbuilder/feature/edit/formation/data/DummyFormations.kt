package com.wiseduck.squadbuilder.feature.edit.formation.data

import com.wiseduck.squadbuilder.core.model.Placement
import com.wiseduck.squadbuilder.feature.edit.formation.data.FormationConstants.SLOT_ZONES_BOUNDS
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

fun createDefaultPlayers(quarter: Int = 1): ImmutableList<Placement> {
    val formation433 =
        listOf(
            "GK",
            "LB", "LCB", "RCB", "RB",
            "LCM", "CDM", "RCM",
            "LW", "ST", "RW",
        )

    return formation433.mapIndexed { index, positionKey ->
        val bounds =
            SLOT_ZONES_BOUNDS[positionKey]
                ?: PositionBounds(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f)

        Placement(
            slotId = index + 1,
            playerId = null,
            playerName = "Player",
            playerPosition = positionKey,
            playerBackNumber = "+",
            quarter = quarter,
            coordX = bounds.centerX,
            coordY = bounds.centerY,
        )
    }.toPersistentList()
}

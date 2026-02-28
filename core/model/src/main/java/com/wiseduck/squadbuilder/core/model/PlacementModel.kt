package com.wiseduck.squadbuilder.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlacementModel(
    val slotId: Int,
    val playerId: Int?,
    val playerName: String,
    val playerPosition: String,
    val playerBackNumber: String,
    val quarter: Int,
    val coordX: Float,
    val coordY: Float,
)

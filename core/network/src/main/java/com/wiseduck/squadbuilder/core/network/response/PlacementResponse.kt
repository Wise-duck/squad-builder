package com.wiseduck.squadbuilder.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class PlacementResponse(
    val playerId: Int,
    val playerName: String,
    val playerPosition: String,
    val playerBackNumber: Int,
    val quarter: Int,
    val coordX: Float,
    val coordY: Float
)
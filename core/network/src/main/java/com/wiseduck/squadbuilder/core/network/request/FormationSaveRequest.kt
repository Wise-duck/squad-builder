package com.wiseduck.squadbuilder.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class PlacementSaveRequest(
    val playerId: Int,
    val quarter: Int,
    val coordX: Int,
    val coordY: Int
)

@Serializable
data class FormationSaveRequest(
    val teamId: Int,
    val name: String,
    val placements: List<PlacementSaveRequest>,
    val referees: Map<String, String>
)

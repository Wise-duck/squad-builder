package com.wiseduck.squadbuilder.core.model

data class PlacementSaveModel(
    val playerId: Int,
    val quarter: Int,
    val coordX: Int,
    val coordY: Int
)

data class FormationSaveModel(
    val teamId: Int,
    val name: String,
    val placements: List<PlacementSaveModel>,
    val referees: Map<String, String>
)

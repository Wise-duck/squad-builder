package com.wiseduck.squadbuilder.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlacementSaveModel(
    val playerId: Int,
    val quarter: Int,
    val coordX: Int,
    val coordY: Int,
)

@Immutable
data class FormationSaveModel(
    val teamId: Int,
    val name: String,
    val placements: List<PlacementSaveModel>,
    val referees: Map<String, String>,
)

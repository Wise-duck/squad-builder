package com.wiseduck.squadbuilder.core.model

data class FormationDetailModel(
    val formationId: Int,
    val name: String,
    val createdAt: String,
    val teamId: Int,
    val teamName: String,
    val placements: List<PlacementModel>,
    val referees: Map<String, String>,
)

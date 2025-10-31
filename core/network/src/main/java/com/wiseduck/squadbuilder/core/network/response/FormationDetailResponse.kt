package com.wiseduck.squadbuilder.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class FormationDetailResponse(
    val formationId: Int,
    val name: String,
    val createdAt: String,
    val teamId: Int,
    val teamName: String,
    val placements: List<PlacementResponse>
)

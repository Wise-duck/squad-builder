package com.wiseduck.squadbuilder.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class FormationDetail(
    val formationId: Int,
    val name: String,
    val createdAt: String,
    val teamId: Int,
    val teamName: String,
    val placements: List<Placement>,
    val referees: Map<String, String>,
)

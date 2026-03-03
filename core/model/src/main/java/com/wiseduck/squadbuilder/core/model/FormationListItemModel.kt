package com.wiseduck.squadbuilder.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class FormationListItemModel(
    val formationId: Int,
    val name: String,
    val createdAt: String,
)

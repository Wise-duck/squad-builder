package com.wiseduck.squadbuilder.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class TeamPlayerModel(
    val id: Int,
    val teamId: Int,
    val name: String,
    val backNumber: Int,
    val position: String,
)

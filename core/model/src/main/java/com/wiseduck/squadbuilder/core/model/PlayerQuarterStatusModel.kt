package com.wiseduck.squadbuilder.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlayerQuarterStatusModel(
    val playerId: Int,
    val playerName: String,
    val quarters: List<Int>,
    val backNumber: Int,
    val position: String,
)

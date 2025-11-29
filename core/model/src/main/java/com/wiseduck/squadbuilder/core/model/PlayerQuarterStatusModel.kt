package com.wiseduck.squadbuilder.core.model

data class PlayerQuarterStatusModel(
    val playerId: Int,
    val playerName: String,
    val quarters: List<Int>,
    val backNumber: Int,
    val position: String,
)

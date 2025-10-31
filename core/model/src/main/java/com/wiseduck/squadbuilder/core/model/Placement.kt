package com.wiseduck.squadbuilder.core.model

data class Placement (
    val playerId: Int,
    val playerName: String,
    val playerPosition: String,
    val playerBackNumber: String,
    val coordX: Float,
    val coordY: Float
)
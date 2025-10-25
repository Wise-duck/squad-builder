package com.wiseduck.squadbuilder.core.model

data class PlayerModel(
    val id: Int,
    val teamId: Int,
    val name: String,
    val backNumber: Int,
    val position: String,
)
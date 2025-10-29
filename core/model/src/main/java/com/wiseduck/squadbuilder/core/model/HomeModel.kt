package com.wiseduck.squadbuilder.core.model

data class HomeModel(
    val teams: List<TeamModel> = emptyList()
)

data class TeamModel(
    val teamId: Int,
    val name: String = "",
    val ownerId: String = "",
    val ownerEmail: String? = null,
    val createdAt: String = ""
)
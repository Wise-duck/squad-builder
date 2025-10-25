package com.wiseduck.squadbuilder.core.model

data class HomeModel(
    val myTeamModel: List<MyTeamModel> = emptyList()
)

data class MyTeamModel(
    val teamName: String = "",
    val createdAt: String = ""
)
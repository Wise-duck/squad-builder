package com.wiseduck.squadbuilder.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    @SerialName("teams")
    val teams: List<MyTeams>
)

@Serializable
data class MyTeams(
    @SerialName("teamId")
    val teamId: Int,
    @SerialName("teamName")
    val teamName: String,
    @SerialName("createdAt")
    val createdAt: String
)
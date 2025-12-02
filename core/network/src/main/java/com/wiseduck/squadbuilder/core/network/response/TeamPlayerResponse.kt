package com.wiseduck.squadbuilder.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamPlayerResponse(
    @SerialName("playerId")
    val playerId: Int,
    @SerialName("teamId")
    val teamId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("position")
    val position: String,
    @SerialName("number")
    val number: Int,
)

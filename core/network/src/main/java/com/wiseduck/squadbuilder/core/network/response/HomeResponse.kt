package com.wiseduck.squadbuilder.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    @SerialName("teams")
    val teams: List<Team>
)

@Serializable
data class Team(
    @SerialName("teamId")
    val teamId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("ownerId")
    val ownerId: String,
    @SerialName("ownerEmail")
    val ownerEmail: String,
    @SerialName("createdAt")
    val createdAt: String
)

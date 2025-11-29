package com.wiseduck.squadbuilder.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamResponse(
    @SerialName("teamId")
    val teamId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("ownerId")
    val ownerId: String,
    @SerialName("ownerEmail")
    val ownerEmail: String? = null,
    @SerialName("createdAt")
    val createdAt: String,
)

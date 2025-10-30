package com.wiseduck.squadbuilder.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamCreateRequest(
    @SerialName("name")
    val name: String
)
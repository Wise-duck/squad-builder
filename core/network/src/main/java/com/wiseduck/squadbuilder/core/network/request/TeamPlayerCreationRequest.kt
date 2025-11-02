package com.wiseduck.squadbuilder.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamPlayerCreationRequest(
    @SerialName("name")
    val name: String,
    @SerialName("position")
    val position: String,
    @SerialName("backNumber")
    val backNumber: Int
)
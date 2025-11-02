package com.wiseduck.squadbuilder.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormationListItemResponse(
    @SerialName("formationId")
    val formationId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("createdAt")
    val createdAt: String
)
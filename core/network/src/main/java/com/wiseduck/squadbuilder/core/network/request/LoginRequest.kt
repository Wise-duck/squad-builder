package com.wiseduck.squadbuilder.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("provider")
    val provider: String,
    @SerialName("accessToken")
    val accessToken: String,
)

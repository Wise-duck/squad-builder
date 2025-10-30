package com.wiseduck.squadbuilder.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class HomeModel(
    val teams: List<TeamModel> = emptyList()
)

@Parcelize
data class TeamModel(
    val teamId: Int,
    val name: String = "",
    val ownerId: String = "",
    val ownerEmail: String? = null,
    val createdAt: String = ""
) : Parcelable

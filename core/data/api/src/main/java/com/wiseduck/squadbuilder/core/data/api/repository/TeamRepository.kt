package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.Team

interface TeamRepository {
    suspend fun createTeam(name: String): Result<Team>

    suspend fun getTeams(): Result<List<Team>>

    suspend fun deleteTeam(teamId: Int): Result<Unit>
}

package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.TeamModel

interface TeamRepository {
    suspend fun createTeam(name: String) : Result<TeamModel>
    suspend fun getTeams() : Result<List<TeamModel>>
    suspend fun deleteTeam(teamId: Int) : Result<Unit>
}
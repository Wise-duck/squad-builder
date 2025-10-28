package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.HomeModel

interface TeamRepository {
    suspend fun getTeams() : Result<HomeModel>
}
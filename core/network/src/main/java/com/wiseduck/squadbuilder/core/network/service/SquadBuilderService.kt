package com.wiseduck.squadbuilder.core.network.service

import retrofit2.http.GET
import retrofit2.http.POST

interface SquadBuilderService {
    // PUBLIC ENDPOINTS
    @POST("api/auth/login")
    suspend fun login()

    @POST("api/auth/refresh")
    suspend fun refreshToken()

    // PRIVATE ENDPOINTS
    @GET("api/teams")
    suspend fun getMyTeams()
}
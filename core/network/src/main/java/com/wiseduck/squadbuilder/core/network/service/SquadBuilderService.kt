package com.wiseduck.squadbuilder.core.network.service

import com.wiseduck.squadbuilder.core.network.request.RefreshTokenRequest
import com.wiseduck.squadbuilder.core.network.response.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SquadBuilderService {
    // PUBLIC ENDPOINTS
    @POST("api/auth/login")
    suspend fun login()

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest) : RefreshTokenResponse

    // PRIVATE ENDPOINTS
    @GET("api/teams")
    suspend fun getMyTeams()
}
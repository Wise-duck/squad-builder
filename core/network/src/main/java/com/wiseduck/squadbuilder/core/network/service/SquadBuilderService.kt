package com.wiseduck.squadbuilder.core.network.service

import com.wiseduck.squadbuilder.core.network.request.LoginRequest
import com.wiseduck.squadbuilder.core.network.request.RefreshTokenRequest
import com.wiseduck.squadbuilder.core.network.response.LoginResponse
import com.wiseduck.squadbuilder.core.network.response.RefreshTokenResponse
import com.wiseduck.squadbuilder.core.network.response.TeamResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SquadBuilderService {
    // PUBLIC ENDPOINTS [AUTH]
    @POST("api/auth/social-login")
    suspend fun login(@Body loginRequest: LoginRequest) : LoginResponse

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest) : RefreshTokenResponse

    // PRIVATE ENDPOINTS [AUTH]
    @POST("api/auth/logout")
    suspend fun logout()

    @DELETE("api/auth/withdraw")
    suspend fun withdraw()

    // PRIVATE ENDPOINTS [TEAM]
    @GET("api/teams")
    suspend fun getTeams(): List<TeamResponse>

    @DELETE("api/teams/{teamId}")
    suspend fun deleteTeam(
        @Path("teamId") teamId: Int
    )
}
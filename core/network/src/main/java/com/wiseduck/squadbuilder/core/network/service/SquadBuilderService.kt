package com.wiseduck.squadbuilder.core.network.service

import com.wiseduck.squadbuilder.core.network.request.FormationSaveRequest
import com.wiseduck.squadbuilder.core.network.request.LoginRequest
import com.wiseduck.squadbuilder.core.network.request.RefreshTokenRequest
import com.wiseduck.squadbuilder.core.network.request.TeamCreationRequest
import com.wiseduck.squadbuilder.core.network.request.TeamPlayerCreationRequest
import com.wiseduck.squadbuilder.core.network.request.TeamPlayerUpdateRequest
import com.wiseduck.squadbuilder.core.network.response.FormationDetailResponse
import com.wiseduck.squadbuilder.core.network.response.FormationListItemResponse
import com.wiseduck.squadbuilder.core.network.response.LoginResponse
import com.wiseduck.squadbuilder.core.network.response.RefreshTokenResponse
import com.wiseduck.squadbuilder.core.network.response.TeamPlayerResponse
import com.wiseduck.squadbuilder.core.network.response.TeamResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SquadBuilderService {
    // [AUTH] [PUBLIC] ENDPOINTS
    @POST("api/auth/social-login")
    suspend fun login(@Body loginRequest: LoginRequest) : LoginResponse

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest) : RefreshTokenResponse

    // [AUTH] [PRIVATE] ENDPOINTS
    @POST("api/auth/logout")
    suspend fun logout()

    @DELETE("api/auth/withdraw")
    suspend fun withdraw()

    // [TEAM] [PRIVATE] ENDPOINTS
    @POST("api/teams")
    suspend fun createTeam(@Body request: TeamCreationRequest) : TeamResponse

    @GET("api/teams")
    suspend fun getTeams(): List<TeamResponse>

    @DELETE("api/teams/{teamId}")
    suspend fun deleteTeam(
        @Path("teamId") teamId: Int
    )

    // [TEAM PLAYER] [PRIVATE] ENDPOINTS
    @PUT("api/teams/{teamId}/players/{playerId}")
    suspend fun updateTeamPlayer(
        @Path("teamId") teamId: Int,
        @Path("playerId") playerId: Int,
        @Body() teamPlayerUpdateRequest: TeamPlayerUpdateRequest
    ) : TeamPlayerResponse

    @DELETE("api/teams/{teamId}/players/{playerId}")
    suspend fun deleteTeamPlayer(
        @Path("teamId") teamId: Int,
        @Path("playerId") playerId: Int,
    )

    @GET("api/teams/{teamId}/players")
    suspend fun getTeamPlayers(
        @Path("teamId") teamId: Int
    ) : List<TeamPlayerResponse>

    @POST("api/teams/{teamId}/players")
    suspend fun createTeamPlayer(
        @Path("teamId") teamId: Int,
        @Body() teamPlayerCreationRequest: TeamPlayerCreationRequest
    ) : TeamPlayerResponse

    // [FORMATION] [PRIVATE] ENDPOINTS
    @GET("api/formation")
    suspend fun getFormationList(
        @Query("teamId") teamId: Int
    ) : List<FormationListItemResponse>

    @GET("api/formation/{formationId}")
    suspend fun getFormationDetail(
        @Path("formationId") formationId: Int
    ): FormationDetailResponse

    @POST("api/formation")
    suspend fun createFormation(
        @Body request: FormationSaveRequest
    ): Unit

    @PUT("api/formation/{formationId}")
    suspend fun updateFormation(
        @Path("formationId") formationId: Int,
        @Body request: FormationSaveRequest
    ): Unit

    @DELETE("api/formation/{formationId}")
    suspend fun deleteFormation(
        @Path("formationId") formationId: Int
    ): Unit
}
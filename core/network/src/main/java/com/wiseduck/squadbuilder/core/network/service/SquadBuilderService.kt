package com.wiseduck.squadbuilder.core.network.service

import com.wiseduck.squadbuilder.core.network.request.LoginRequest
import com.wiseduck.squadbuilder.core.network.request.RefreshTokenRequest
import com.wiseduck.squadbuilder.core.network.response.HomeResponse
import com.wiseduck.squadbuilder.core.network.response.LoginResponse
import com.wiseduck.squadbuilder.core.network.response.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface SquadBuilderService {
    // PUBLIC ENDPOINTS
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : LoginResponse

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest) : RefreshTokenResponse

    // PRIVATE ENDPOINTS
    @POST("api/auth/logout")
    suspend fun logout()

    @DELETE("api/auth/withdraw")
    suspend fun withdraw()

    @GET("api/teams")
    suspend fun getTeams(): HomeResponse
}
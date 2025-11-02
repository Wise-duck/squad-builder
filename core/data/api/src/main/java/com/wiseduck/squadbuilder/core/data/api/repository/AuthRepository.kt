package com.wiseduck.squadbuilder.core.data.api.repository

interface AuthRepository {
    suspend fun login(accessToken: String) : Result<Unit>
    suspend fun logout() : Result<Unit>
    suspend fun withdraw() : Result<Unit>
}
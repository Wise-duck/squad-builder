package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.LoginState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(accessToken: String) : Result<Unit>
    suspend fun logout() : Result<Unit>
    suspend fun withdraw() : Result<Unit>
    val loginState: Flow<LoginState>
}
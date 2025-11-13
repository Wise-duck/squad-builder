package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.AuthRepository
import com.wiseduck.squadbuilder.core.datastore.api.datasource.TokenDataSource
import com.wiseduck.squadbuilder.core.datastore.api.datasource.UserDataSource
import com.wiseduck.squadbuilder.core.model.LoginState
import com.wiseduck.squadbuilder.core.network.request.LoginRequest
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PROVIDER = "kakao"

internal class AuthRepositoryImpl @Inject constructor(
    private val dataSource: TokenDataSource,
    private val service: SquadBuilderService,
    private val userDataSource: UserDataSource,
) : AuthRepository {

    override val loginState: Flow<LoginState> = dataSource.accessToken.map { accessToken ->
        if (accessToken.isBlank()) {
            LoginState.NOT_YET
        } else {
            LoginState.LOGGED_IN
        }
    }

    override suspend fun login(accessToken: String): Result<Unit> = runCatching {
        val response = service.login(
            LoginRequest(
                provider = PROVIDER,
                accessToken = accessToken
            )
        )

        dataSource.apply {
            setAccessToken(response.accessToken)
            setRefreshToken(response.refreshToken)
        }

        userDataSource.apply {
            setUsername(response.username)
        }
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        service.logout()
        dataSource.resetTokens()
    }

    override suspend fun withdraw(): Result<Unit> = runCatching {
        service.withdraw()
        dataSource.resetTokens()
    }
}
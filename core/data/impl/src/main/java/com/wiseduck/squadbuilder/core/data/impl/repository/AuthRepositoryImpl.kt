package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.AuthRepository
import com.wiseduck.squadbuilder.core.datastore.api.datasource.TokenDataSource
import com.wiseduck.squadbuilder.core.network.request.LoginRequest
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import javax.inject.Inject

private const val PROVIDER = "kakao"

internal class AuthRepositoryImpl @Inject constructor(
    private val dataSource: TokenDataSource,
    private val service: SquadBuilderService
) : AuthRepository {

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
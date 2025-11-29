package com.wiseduck.squadbuilder.core.network.token

import com.wiseduck.squadbuilder.core.datastore.api.datasource.TokenDataSource
import com.wiseduck.squadbuilder.core.network.request.RefreshTokenRequest
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import jakarta.inject.Provider
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val dataSource: TokenDataSource,
    private val service: Provider<SquadBuilderService>,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? { return runBlocking {
        try {
            val refreshToken = dataSource.getRefreshToken()

            if (refreshToken.isBlank()) {
                dataSource.resetTokens()
                return@runBlocking null
            }

            val refreshTokenRequest = RefreshTokenRequest(refreshToken)

            val refreshTokenResponse = service.get().refreshToken(refreshTokenRequest)

            dataSource.apply {
                setAccessToken(refreshTokenResponse.accessToken)
                setRefreshToken(refreshTokenResponse.refreshToken)
            }

            response.request.newBuilder()
                .header("Authorization", "Bearer ${refreshTokenResponse.accessToken}")
                .build()
        } catch (e: Exception) {
            dataSource.resetTokens()

            return@runBlocking null
        }
    }
    }
}

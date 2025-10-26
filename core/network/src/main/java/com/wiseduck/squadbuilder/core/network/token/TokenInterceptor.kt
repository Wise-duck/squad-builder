package com.wiseduck.squadbuilder.core.network.token

import com.wiseduck.squadbuilder.core.datastore.api.datasource.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TokenInterceptor @Inject constructor(
    private val dataSource: TokenDataSource
) : Interceptor {

    private val publicEndpoints = setOf(
        "api/auth/login",
        "api/auth/refresh"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val isPublic = publicEndpoints.any { url.contains(it) }

        if (isPublic) {
            return chain.proceed(request)
        } else {
            val accessToken = runBlocking {
                dataSource.getAccessToken()
            }

            return chain.proceed(
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            )
        }
    }
}
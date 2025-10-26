package com.wiseduck.squadbuilder.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.wiseduck.squadbuilder.core.network.BuildConfig
import com.wiseduck.squadbuilder.core.network.token.TokenInterceptor
import com.wiseduck.squadbuilder.core.network.token.TokenAuthenticator
import com.wiseduck.squadbuilder.core.network.service.SquadBuilderService
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val MAX_TIMEOUT_MILLIS = 10_000L

private val jsonRule = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
}

private val jsonConverterFactory = jsonRule.asConverterFactory("application/json".toMediaType())

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    internal fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .addInterceptor(tokenInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideSquadBuilderService(
        retrofit: Retrofit,
    ): SquadBuilderService {
        return retrofit.create()
    }
}
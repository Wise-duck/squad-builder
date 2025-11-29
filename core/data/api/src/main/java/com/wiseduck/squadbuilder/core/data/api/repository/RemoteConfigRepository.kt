package com.wiseduck.squadbuilder.core.data.api.repository

interface RemoteConfigRepository {
    suspend fun getLatestVersion(): Result<String>

    suspend fun shouldUpdate(): Result<Boolean>
}

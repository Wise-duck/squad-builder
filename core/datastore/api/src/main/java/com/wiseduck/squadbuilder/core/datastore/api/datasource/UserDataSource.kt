package com.wiseduck.squadbuilder.core.datastore.api.datasource

import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    val username: Flow<String>
    suspend fun setUsername(username: String)
    suspend fun getUsername(): String
}
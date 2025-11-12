package com.wiseduck.squadbuilder.core.datastore.api.datasource

import com.wiseduck.squadbuilder.core.model.OnboardingState
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    val username: Flow<String>
    suspend fun setUsername(username: String)
    suspend fun getUsername(): String
    val onboardingState: Flow<OnboardingState>
    suspend fun setOnboardingCompleted(completed: Boolean)
}
package com.wiseduck.squadbuilder.core.data.api.repository

import com.wiseduck.squadbuilder.core.model.OnboardingState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val onboardingState: Flow<OnboardingState>

    suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun getUserName(): String
}

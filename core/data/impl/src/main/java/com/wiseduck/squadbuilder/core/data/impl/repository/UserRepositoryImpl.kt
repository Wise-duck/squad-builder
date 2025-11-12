package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.UserRepository
import com.wiseduck.squadbuilder.core.datastore.api.datasource.UserDataSource
import com.wiseduck.squadbuilder.core.model.OnboardingState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository{

    override val onboardingState: Flow<OnboardingState> = userDataSource.onboardingState

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        userDataSource.setOnboardingCompleted(completed)
    }

    override suspend fun getUserName(): String = userDataSource.getUsername()
}
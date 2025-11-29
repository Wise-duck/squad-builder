package com.wiseduck.squadbuilder.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wiseduck.squadbuilder.core.datastore.api.datasource.UserDataSource
import com.wiseduck.squadbuilder.core.datastore.impl.di.UserDataStore
import com.wiseduck.squadbuilder.core.model.OnboardingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    @UserDataStore private val userDataStore: DataStore<Preferences>,
) : UserDataSource {
    override val onboardingState: Flow<OnboardingState> =
        userDataStore.data.map { preferences ->
            when (preferences[ONBOARDING_STATE] ?: false) {
                false -> OnboardingState.NOT_YET
                true -> OnboardingState.COMPLETED
            }
        }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        userDataStore.edit { preferences ->
            preferences[ONBOARDING_STATE] = completed
        }
    }

    override val username: Flow<String> =
        userDataStore.data.map { preferences ->
            preferences[USER_NAME] ?: ""
        }

    override suspend fun setUsername(username: String) {
        userDataStore.edit { preferences ->
            preferences[USER_NAME] = username
        }
    }

    override suspend fun getUsername(): String = username.first()

    private companion object {
        val USER_NAME = stringPreferencesKey("USER_NAME")
        val ONBOARDING_STATE = booleanPreferencesKey("ONBOARDING_STATE")
    }
}

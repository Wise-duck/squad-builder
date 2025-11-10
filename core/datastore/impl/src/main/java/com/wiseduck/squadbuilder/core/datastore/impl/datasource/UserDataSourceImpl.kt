package com.wiseduck.squadbuilder.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wiseduck.squadbuilder.core.datastore.api.datasource.UserDataSource
import com.wiseduck.squadbuilder.core.datastore.impl.di.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    @UserDataStore private val userDataStore: DataStore<Preferences>
) : UserDataSource {
    override val username: Flow<String> = userDataStore.data.map { preferences ->
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
    }
}
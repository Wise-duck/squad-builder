package com.wiseduck.squadbuilder.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wiseduck.squadbuilder.core.datastore.api.datasource.TokenDataSource
import com.wiseduck.squadbuilder.core.datastore.impl.di.TokenDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataSourceImpl @Inject constructor(
    @TokenDataStore private val dataStore: DataStore<Preferences>,
) : TokenDataSource {
    override val accessToken: Flow<String> =
        dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    override val refreshToken: Flow<String> =
        dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }

    override suspend fun getAccessToken(): String = accessToken.first()

    override suspend fun getRefreshToken(): String = refreshToken.first()

    override suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun resetTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}

package com.wiseduck.squadbuilder.core.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.wiseduck.squadbuilder.core.datastore.api.datasource.TokenDataSource
import com.wiseduck.squadbuilder.core.datastore.impl.datasource.TokenDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.tokenDataStore by preferencesDataStore(name = "TOKEN_DATASTORE")

    @TokenDataStore
    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.tokenDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindDataStoreModule {

    @Binds
    @Singleton
    abstract fun bindTokenDataSource(
        tokenDataSourceImpl: TokenDataSourceImpl
    ) : TokenDataSource
}
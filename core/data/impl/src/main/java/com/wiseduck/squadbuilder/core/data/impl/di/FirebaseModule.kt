package com.wiseduck.squadbuilder.core.data.impl.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.wiseduck.squadbuilder.core.data.impl.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseModule {

    @Singleton
    @Provides
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0L else 3600L
            })
        }
    }
}
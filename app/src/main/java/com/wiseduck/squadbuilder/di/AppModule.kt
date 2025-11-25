package com.wiseduck.squadbuilder.di

import com.wiseduck.squadbuilder.BuildConfig
import com.wiseduck.squadbuilder.core.common.di.AdmobBannerId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @AdmobBannerId
    fun provideAdmobBannerId(): String {
        return BuildConfig.ADMOB_BANNER_ID
    }
}
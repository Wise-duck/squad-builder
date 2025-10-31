package com.wiseduck.squadbuilder.core.data.impl.di

import com.wiseduck.squadbuilder.core.data.api.repository.AuthRepository
import com.wiseduck.squadbuilder.core.data.api.repository.FormationRepository
import com.wiseduck.squadbuilder.core.data.api.repository.PlayerRepository
import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.data.impl.repository.AuthRepositoryImpl
import com.wiseduck.squadbuilder.core.data.impl.repository.FormationRepositoryImpl
import com.wiseduck.squadbuilder.core.data.impl.repository.PlayerRepositoryImpl
import com.wiseduck.squadbuilder.core.data.impl.repository.TeamRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl) : AuthRepository

    @Binds
    @Singleton
    abstract fun bindTeamRepository(teamRepositoryImpl: TeamRepositoryImpl) : TeamRepository

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(playerRepositoryImpl: PlayerRepositoryImpl) : PlayerRepository

    @Binds
    @Singleton
    abstract fun bindFormationRepository(formationRepositoryImpl: FormationRepositoryImpl) : FormationRepository
}
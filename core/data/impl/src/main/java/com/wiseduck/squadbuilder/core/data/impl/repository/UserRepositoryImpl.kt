package com.wiseduck.squadbuilder.core.data.impl.repository

import com.wiseduck.squadbuilder.core.data.api.repository.UserRepository
import com.wiseduck.squadbuilder.core.datastore.api.datasource.UserDataSource
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository{

    override suspend fun getUserName(): String = userDataSource.getUsername()
}
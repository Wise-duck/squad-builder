package com.wiseduck.squadbuilder.core.data.api.repository

interface UserRepository {
    suspend fun getUserName(): String
}
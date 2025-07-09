package com.wiseduck.squadbuilder.repository

import androidx.lifecycle.LiveData
import com.wiseduck.squadbuilder.dao.PlayerDao
import com.wiseduck.squadbuilder.data.Formation
import com.wiseduck.squadbuilder.data.FormationWithPlayers
import com.wiseduck.squadbuilder.data.Player

class PlayerRepository(private val playerDao: PlayerDao) {

    fun getAllPlayers(): LiveData<List<Player>> {
        return playerDao.getAllPlayers()
    }

    fun getPlayersForFormation(formationId: Int): LiveData<List<Player>> {
        return playerDao.getPlayersForFormation(formationId)
    }

    fun getFormationWithPlayers(formationId: Int): LiveData<FormationWithPlayers> {
        return playerDao.getFormationWithPlayers(formationId)
    }

    fun getAllFormationsWithPlayers(): LiveData<List<FormationWithPlayers>> {
        return playerDao.getAllFormationsWithPlayers()
    }

    suspend fun insertFormation(formation: Formation): Long {
        return playerDao.insertFormation(formation)
    }

    suspend fun insertPlayer(player: Player): Long {
        return playerDao.insertPlayer(player)
    }

    suspend fun insertFormationWithPlayers(formation: Formation, players: List<Player>) {
        val formationId = playerDao.insertFormation(formation).toInt()
        players.forEach { it.formationId = formationId }
    }

    suspend fun updateFormation(formation: Formation) {
        playerDao.updateFormation(formation)  // 업데이트 메서드 호출
    }

    suspend fun updatePlayer(player: Player) {
        playerDao.updatePlayer(player)
    }

    suspend fun deleteFormation(formation: Formation) {
        playerDao.deletePlayersByFormationId(formation.id)
        playerDao.deleteFormation(formation)
    }
}

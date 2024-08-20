package com.example.squadbuilder.repository

import androidx.lifecycle.LiveData
import com.example.squadbuilder.dao.PlayerDao
import com.example.squadbuilder.data.Formation
import com.example.squadbuilder.data.Player

class PlayerRepository(private val playerDao: PlayerDao) {

    fun getAllPlayers(): LiveData<List<Player>> {
        return playerDao.getAllPlayers()
    }

    fun getPlayersForFormation(formationId: Int): LiveData<List<Player>> {
        return playerDao.getPlayersForFormation(formationId)
    }

    suspend fun insertFormationWithPlayers(formation: Formation, players: List<Player>) {
        val formationId = playerDao.insertFormation(formation).toInt()
        players.forEach { it.formationId = formationId }
        playerDao.insertPlayers(players)
    }

    suspend fun updatePlayer(player: Player) {
        playerDao.updatePlayer(player)
    }

    suspend fun deleteFormation(formation: Formation) {
        playerDao.deletePlayersByFormationId(formation.id)
        playerDao.deleteFormation(formation)
    }
}

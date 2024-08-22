package com.example.squadbuilder.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.squadbuilder.data.Formation
import com.example.squadbuilder.data.FormationWithPlayers
import com.example.squadbuilder.data.Player

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormation(formation: Formation): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player): Long

    @Update
    suspend fun updatePlayer(player: Player)

    @Query("SELECT * FROM players WHERE formationId = :formationId")
    fun getPlayersForFormation(formationId: Int): LiveData<List<Player>>

    @Query("SELECT * FROM players")
    fun getAllPlayers(): LiveData<List<Player>>

    @Transaction
    @Query("SELECT * FROM formations WHERE id = :formationId")
    fun getFormationWithPlayers(formationId: Int): LiveData<FormationWithPlayers>

    @Transaction
    @Query("SELECT * FROM formations")
    fun getAllFormationsWithPlayers(): LiveData<List<FormationWithPlayers>>

    @Delete
    suspend fun deleteFormation(formation: Formation)

    @Query("DELETE FROM players WHERE formationId = :formationId")
    suspend fun deletePlayersByFormationId(formationId: Int)
}

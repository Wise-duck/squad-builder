package com.example.squadbuilder.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.squadbuilder.data.Formation
import com.example.squadbuilder.data.FormationWithPlayers
import com.example.squadbuilder.data.Player

@Dao
interface FormationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormation(formation: Formation): Long  // ID 반환

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<Player>)

    // 모든 포메이션과 관련 플레이어를 가져오는 메서드
    @Transaction
    @Query("SELECT * FROM formations")
    suspend fun getAllFormationsWithPlayers(): List<FormationWithPlayers>

    // 특정 포메이션과 그에 속한 플레이어들을 조회하는 메서드
    @Transaction
    @Query("SELECT * FROM formations WHERE id = :formationId")
    suspend fun getFormationWithPlayers(formationId: Int): FormationWithPlayers

    @Delete
    suspend fun deleteFormation(formation: Formation)

    @Query("DELETE FROM players WHERE formationId = :formationId")
    suspend fun deletePlayersByFormationId(formationId: Int)
}

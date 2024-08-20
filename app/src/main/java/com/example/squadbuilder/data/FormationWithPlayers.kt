package com.example.squadbuilder.data

import androidx.room.Embedded
import androidx.room.Relation

data class FormationWithPlayers(
    @Embedded val formation: Formation,
    @Relation(
        parentColumn = "id",
        entityColumn = "formationId"
    )
    val players: List<Player>
)

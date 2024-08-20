package com.example.squadbuilder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formations")
data class Formation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val teamName: String,
    val creationDate: String
)

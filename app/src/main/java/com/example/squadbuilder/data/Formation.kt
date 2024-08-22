package com.example.squadbuilder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formations")
data class Formation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val teamName: String,
    val creationDate: String,
    val teamPhotoUri: String? = null  // 팀 사진 URI 추가, 초기값은 null
)

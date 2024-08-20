package com.example.squadbuilder.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "players",
    foreignKeys = [
        ForeignKey(
            entity = Formation::class,
            parentColumns = ["id"],
            childColumns = ["formationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["formationId"])]
)
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var formationId: Int,  // Formation의 ID 참조
    var name: String,  // 플레이어 이름
    var number: Int,  // 플레이어 등 번호
    var position: String,  // 플레이어 포지션
    var x: Float,  // 플레이어 위치(x 좌표 값)
    var y: Float,  // 플레이어 위치(y 좌표 값)
    var photoUri: String,  // 플레이어 얼굴 사진 설정 기능 구현 시 사용 예
)




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
    val formationId: Int,  // Formation의 ID 참조
    val name: String,  // 플레이어 이름
    val number: Int,  // 플레이어 등 번호
    var x: Float,  // 플레이어 위치(x 좌표 값)
    var y: Float,  // 플레이어 위치(y 좌표 값)
    val photoUri: String,  // 플레이어 얼굴 사진 설정 기능 구현 시 사용 예정
    val soccerPosition: SoccerPosition?,  // 풋살 포메이션 생성 시 이 값은 null
    val footballPosition: FootballPosition?   // 축구 포메이션 생성 시 이 값은 null
)

enum class SoccerPosition {
    // 수정 필요
    GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD
}

enum class FootballPosition {
    // 포지션 추가 필요
}

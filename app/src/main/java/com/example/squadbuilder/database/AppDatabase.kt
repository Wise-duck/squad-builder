package com.example.squadbuilder.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.squadbuilder.dao.FormationDao
import com.example.squadbuilder.data.Formation
import com.example.squadbuilder.data.Player

// 엔티티 클래스, 데이터베이스 버전 정의
@Database(entities = [Formation::class, Player::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun formationDao(): FormationDao
    // 추후 필요 시 dao 사용
    // abstract fun playerDao(): PlayerDao

    companion object {
        // INSTANCE 변수를 통해 데이터베이스의 싱글톤 인스턴스를 관리
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 데이터베이스의 싱글톤 인스턴스 반환
        fun getDatabase(context: Context): AppDatabase {

            // synchronized 키워드로 멀티스레드 환경에서 안전하게 인스턴스 생성
            return INSTANCE ?: synchronized(this) {
                // Room.databaseBuilder를 사용해 데이터베이스 생성 후, formation_database라는 이름으로 저장
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "formation_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

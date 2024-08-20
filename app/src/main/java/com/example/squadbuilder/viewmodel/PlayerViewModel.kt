package com.example.squadbuilder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.squadbuilder.data.Player
import com.example.squadbuilder.database.AppDatabase
import com.example.squadbuilder.repository.PlayerRepository

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlayerRepository

    val players: LiveData<List<Player>>

    init {
        val playerDao = AppDatabase.getDatabase(application).playerDao()
        repository = PlayerRepository(playerDao)
        players = MutableLiveData(createInitialFormation())
    }

    // 수정 필요
    private fun createInitialFormation(): List<Player> {
        return listOf(
            Player(formationId = 0, name = "", number = 0, x = 200f, y = 600f, photoUri = "", position = ""),
            // 4명의 수비수
            Player(formationId = 0, name = "", number = 0, x = 100f, y = 400f, photoUri = "", position = ""),
            Player(formationId = 0, name = "", number = 0, x = 300f, y = 400f, photoUri = "", position = ""),
            Player(formationId = 0, name = "", number = 0, x = 500f, y = 400f, photoUri = "", position = ""),
            Player(formationId = 0, name = "", number = 0, x = 700f, y = 400f, photoUri = "", position = ""),
            // 3명의 미드필더
            Player(formationId = 0, name = "", number = 0, x = 200f, y = 200f, photoUri = "", position = ""),
            Player(formationId = 0, name = "", number = 0, x = 400f, y = 200f, photoUri = "", position = ""),
            Player(formationId = 0, name = "", number = 0, x = 600f, y = 200f, photoUri = "", position = ""),
            // 3명의 공격수
            Player(formationId = 0, name = "", number = 0, x = 200f, y = 100f, photoUri = "", position = ""),
            Player(formationId = 0, name = "", number = 0, x = 400f, y = 100f, photoUri = "", position = ""),
            Player(formationId = 0, name = "", number = 0, x = 600f, y = 100f, photoUri = "", position = "")
        )
    }

    fun resetFormation() {
        (players as MutableLiveData).value = createInitialFormation()
    }
}

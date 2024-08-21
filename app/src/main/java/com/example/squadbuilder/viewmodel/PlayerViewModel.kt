package com.example.squadbuilder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.viewModelScope
import com.example.squadbuilder.data.Formation
import com.example.squadbuilder.data.FormationWithPlayers
import com.example.squadbuilder.data.Player
import com.example.squadbuilder.database.AppDatabase
import com.example.squadbuilder.repository.PlayerRepository
import kotlinx.coroutines.launch

import com.example.squadbuilder.R
import io.github.muddz.styleabletoast.StyleableToast

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlayerRepository

    val players: LiveData<List<Player>>
    val formationsWithPlayers: LiveData<List<FormationWithPlayers>>


    init {
        val playerDao = AppDatabase.getDatabase(application).playerDao()
        repository = PlayerRepository(playerDao)
        players = MutableLiveData(createInitialFormation())
        formationsWithPlayers = repository.getAllFormationsWithPlayers()
    }

    // 수정 필요(추후 배경 수정 시, 그에 맞는 위치로 조정)
    private fun createInitialFormation(): List<Player> {
        return listOf(
            // 1명의 골키퍼
            Player(formationId = 0, name = "Player", number = 1, x = 0.5f, y = 0.8f, photoUri = "", position = "GK"), // 골키퍼

            // 4명의 수비수
            Player(formationId = 0, name = "Player", number = 2, x = 0.2f, y = 0.6f, photoUri = "", position = "LB"), // 왼쪽 풀백
            Player(formationId = 0, name = "Player", number = 3, x = 0.4f, y = 0.6f, photoUri = "", position = "CB"), // 센터백
            Player(formationId = 0, name = "Player", number = 4, x = 0.6f, y = 0.6f, photoUri = "", position = "CB"), // 센터백
            Player(formationId = 0, name = "Player", number = 5, x = 0.8f, y = 0.6f, photoUri = "", position = "RB"), // 오른쪽 풀백

            // 3명의 미드필더
            Player(formationId = 0, name = "Player", number = 6, x = 0.3f, y = 0.4f, photoUri = "", position = "LM"), // 왼쪽 미드필더
            Player(formationId = 0, name = "Player", number = 7, x = 0.5f, y = 0.4f, photoUri = "", position = "CM"),  // 중앙 미드필더
            Player(formationId = 0, name = "Player", number = 8, x = 0.7f, y = 0.4f, photoUri = "", position = "RM"), // 오른쪽 미드필더

            // 3명의 공격수
            Player(formationId = 0, name = "Player", number = 9, x = 0.3f, y = 0.2f, photoUri = "", position = "LW"), // 왼쪽 윙어
            Player(formationId = 0, name = "Player", number = 10, x = 0.5f, y = 0.2f, photoUri = "", position = "ST"), // 스트라이커
            Player(formationId = 0, name = "Player", number = 11, x = 0.7f, y = 0.2f, photoUri = "", position = "RW")  // 오른쪽 윙어
        )
    }

    fun getFormationWithPlayers(formationId: Int): LiveData<FormationWithPlayers> {
        return repository.getFormationWithPlayers(formationId)
    }

    fun saveFormation(teamName: String) {
        // 날짜 데이터 수정 필요
        viewModelScope.launch {
            val formation = Formation(teamName = teamName, creationDate = System.currentTimeMillis().toString())
            val formationId = repository.insertFormation(formation)

            players.value?.forEach { player ->
                player.formationId = formationId.toInt() // 생성된 포메이션 ID를 플레이어에 할당
            }
            players.value?.let {
                repository.insertPlayers(it)
            }
        }
    }

    fun resetFormation() {
        (players as MutableLiveData).value = createInitialFormation()
        StyleableToast.makeText(
            getApplication(),
            "포메이션이 초기화되었습니다.",
            R.style.resetToast
        ).apply {
            setGravity(android.view.Gravity.BOTTOM)
            show()
        }
    }

    fun updatePlayerPosition(player: Player, newX: Float, newY: Float) {
        val updatedPlayers = players.value?.map {
            if (it.number == player.number) {
                it.copy(x = newX, y = newY) // x와 y 값을 새로운 값으로 복사
            } else {
                it
            }
        }
        (players as MutableLiveData).value = updatedPlayers
    }

}

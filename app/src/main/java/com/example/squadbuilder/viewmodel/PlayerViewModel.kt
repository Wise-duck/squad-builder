package com.example.squadbuilder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Locale
import androidx.lifecycle.viewModelScope
import com.example.squadbuilder.data.Formation
import com.example.squadbuilder.data.FormationWithPlayers
import com.example.squadbuilder.data.Player
import com.example.squadbuilder.database.AppDatabase
import com.example.squadbuilder.repository.PlayerRepository
import kotlinx.coroutines.launch
import com.example.squadbuilder.R
import io.github.muddz.styleabletoast.StyleableToast
import java.text.SimpleDateFormat

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlayerRepository

    val players: LiveData<List<Player>>
    val formationsWithPlayers: LiveData<List<FormationWithPlayers>>

    // 팀 프로필 이미지 URI를 관리하는 LiveData
    val teamProfileImageUri: MutableLiveData<String?> = MutableLiveData(null)

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
            Player(id = -1, formationId = 0, name = "Player", number = 1, x = 0.5f, y = 0.88f, photoUri = "", position = "GK"), // 골키퍼

            // 4명의 수비수
            Player(id = -2, formationId = 0, name = "Player", number = 2, x = 0.1f, y = 0.65f, photoUri = "", position = "LB"), // 왼쪽 풀백
            Player(id = -3, formationId = 0, name = "Player", number = 3, x = 0.37f, y = 0.65f, photoUri = "", position = "CB"), // 센터백
            Player(id = -4, formationId = 0, name = "Player", number = 4, x = 0.63f, y = 0.65f, photoUri = "", position = "CB"), // 센터백
            Player(id = -5, formationId = 0, name = "Player", number = 5, x = 0.9f, y = 0.65f, photoUri = "", position = "RB"), // 오른쪽 풀백

            // 3명의 미드필더
            Player(id = -6, formationId = 0, name = "Player", number = 6, x = 0.25f, y = 0.4f, photoUri = "", position = "LM"), // 왼쪽 미드필더
            Player(id = -7, formationId = 0, name = "Player", number = 7, x = 0.5f, y = 0.4f, photoUri = "", position = "CM"),  // 중앙 미드필더
            Player(id = -8, formationId = 0, name = "Player", number = 8, x = 0.75f, y = 0.4f, photoUri = "", position = "RM"), // 오른쪽 미드필더

            // 3명의 공격수
            Player(id = -9, formationId = 0, name = "Player", number = 9, x = 0.2f, y = 0.15f, photoUri = "", position = "LW"), // 왼쪽 윙어
            Player(id = -10, formationId = 0, name = "Player", number = 10, x = 0.5f, y = 0.12f, photoUri = "", position = "ST"), // 스트라이커
            Player(id = -11, formationId = 0, name = "Player", number = 11, x = 0.8f, y = 0.15f, photoUri = "", position = "RW")  // 오른쪽 윙어
        )
    }

    fun updatePlayerDetails(player: Player, newName: String, newNumber: Int, newPosition: String, newPhotoUri: String?) {
        players.value?.let { currentPlayers ->
            val updatedPlayers = currentPlayers.map {
                if (it.id == player.id) {
                    it.copy(name = newName, number = newNumber, position = newPosition, photoUri = newPhotoUri ?: it.photoUri)
                } else {
                    it
                }
            }
            (players as MutableLiveData).value = updatedPlayers

            // 업데이트된 정보를 데이터베이스에 반영
            viewModelScope.launch {
                repository.updatePlayer(updatedPlayers.find { it.id == player.id } ?: return@launch)
            }
        }
    }

    fun getFormationWithPlayers(formationId: Int): LiveData<FormationWithPlayers> {
        return repository.getFormationWithPlayers(formationId)
    }

    fun saveFormation(teamName: String, teamPhotoUri: String?) {
        // 날짜 포맷 설정 (yyyyMMdd 형식)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(System.currentTimeMillis()) // 현재 날짜를 포맷

        viewModelScope.launch {
            val formation = Formation(
                teamName = teamName,
                creationDate = currentDate,
                teamPhotoUri = teamPhotoUri  // 팀 사진 URI 함께 저장
            )
            val formationId = repository.insertFormation(formation).toInt()

            // 플레이어 ID를 업데이트하기 위한 임시 리스트
            val updatedPlayers = mutableListOf<Player>()

            players.value?.forEach { player ->
                val playerToSave = player.copy(
                    formationId = formationId, // 생성된 포메이션 ID로 업데이트
                    id = 0 // ID를 0으로 설정하여 Room이 자동으로 ID를 생성하도록 함
                )

                val savedPlayerId = repository.insertPlayer(playerToSave).toInt()

                // 새로 생성된 ID로 플레이어를 업데이트
                val updatedPlayer = player.copy(id = savedPlayerId, formationId = formationId)
                updatedPlayers.add(updatedPlayer)
            }

            // 뷰 모델 내 플레이어 리스트를 새로 생성된 ID로 업데이트
            (players as MutableLiveData).value = updatedPlayers
        }
    }


    fun resetFormation() {
        (players as MutableLiveData).value = createInitialFormation()
        teamProfileImageUri.value = null  // 팀 프로필 이미지 초기화
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
            if (it.id == player.id) {
                it.copy(x = newX, y = newY) // x와 y 값을 새로운 값으로 복사
            } else {
                it}
            }
        (players as MutableLiveData).value = updatedPlayers
    }

    fun deleteFormation(formation: Formation) {
        viewModelScope.launch {
            repository.deleteFormation(formation)
        }
    }

}

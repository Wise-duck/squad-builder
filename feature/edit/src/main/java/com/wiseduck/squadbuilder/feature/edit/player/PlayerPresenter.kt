package com.wiseduck.squadbuilder.feature.edit.player

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.data.api.repository.PlayerRepository
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.feature.screens.PlayerScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.wiseduck.squadbuilder.feature.edit.R
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class PlayerPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PlayerScreen,
    private val playerRepository: PlayerRepository
) : Presenter<PlayerUiState> {

    @Composable
    override fun present(): PlayerUiState {
        val scope = rememberCoroutineScope()
        val teamName by remember { mutableStateOf(screen.teamName) }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var isShowPlayerCreationSection by remember { mutableStateOf<Boolean>(false) }
        var currentEditingPlayerId by remember { mutableStateOf<Int?>(null) }
        var players by remember { mutableStateOf<List<TeamPlayerModel>>(emptyList()) }
        val errorMessgeServerConnection = stringResource(R.string.load_player_failed_server_connection)

        LaunchedEffect(Unit) {
            playerRepository.getTeamPlayers(teamId = screen.teamId)
                .onSuccess {
                    players = it
                    Log.d("PlayerPresenter", "선수 목록 로드 성공: ${players.size}")
                }
                .onFailure { error ->
                    errorMessage = errorMessgeServerConnection
                    Log.e("PlayerPresenter", "선수 목록 로드 실패: $error")
                }
        }

        fun handleEvent(event: PlayerUiEvent) {
            when (event) {
                is PlayerUiEvent.OnBackButtonClick -> {
                    navigator.pop()
                }

                is PlayerUiEvent.OnTeamPlayerCreationButtonClick -> {
                    isShowPlayerCreationSection = true
                }

                is PlayerUiEvent.OnTeamPlayerCreationCancelButtonClick -> {
                    isShowPlayerCreationSection = false
                }

                is PlayerUiEvent.OnTeamPlayerCreationConfirmButtonClick -> {
                    isLoading = true
                    scope.launch {
                        playerRepository.createTeamPlayer(
                            teamId = screen.teamId,
                            name = event.name,
                            position = event.position,
                            backNumber = event.backNumber
                        )
                            .onSuccess {
                                isLoading = false
                                isShowPlayerCreationSection = false
                                players = players + it
                                Log.d("PlayerPresenter", "선수 생성 성공: ${it.name}")
                            }
                            .onFailure { error ->
                                isLoading = false
                                errorMessage = errorMessgeServerConnection
                                Log.e("PlayerPresenter", "선수 생성 실패: $error")
                            }
                    }
                }

                is PlayerUiEvent.OnTeamPlayerDeleteButtonClick -> {
                    isLoading = true
                    scope.launch {
                        playerRepository.deleteTeamPlayer(
                            teamId = screen.teamId,
                            playerId = event.playerId
                        )
                            .onSuccess {
                                isLoading = false
                                players = players.filter { it.id != event.playerId }
                                currentEditingPlayerId = null
                                Log.d("PlayerPresenter", "선수 삭제 성공")
                            }
                            .onFailure { error ->
                                isLoading = false
                                errorMessage = errorMessgeServerConnection
                                Log.e("PlayerPresenter", "선수 삭제 실패: $error")
                            }
                    }
                }

                is PlayerUiEvent.OnTeamPlayerEditButtonClick -> {
                    currentEditingPlayerId = event.playerId
                }

                is PlayerUiEvent.OnPlayerUpdateConfirm -> {
                    isLoading = true
                    scope.launch {
                        playerRepository.updateTeamPlayer(
                            teamId = screen.teamId,
                            playerId = currentEditingPlayerId!!,
                            name = event.name,
                            position = event.position,
                            backNumber = event.backNumber
                        )
                            .onSuccess { updatedPlayer ->
                                isLoading = false
                                currentEditingPlayerId = null
                                players = players.map {
                                    if (it.id == updatedPlayer.id) updatedPlayer else it
                                }
                                Log.d("PlayerPresenter", "선수 정보 수정 성공: ${updatedPlayer.name}")
                            }
                            .onFailure {
                                isLoading = false
                                currentEditingPlayerId = null
                                errorMessage = errorMessgeServerConnection
                                Log.e("PlayerPresenter", "선수 수정 실패: $it")
                            }
                    }
                }

                is PlayerUiEvent.OnPlayerUpdateCancel -> {
                    currentEditingPlayerId = null
                }

                is PlayerUiEvent.OnDialogCloseButtonClick -> {
                    errorMessage = null
                }

                is PlayerUiEvent.OnTabSelect -> {
                    navigator.goTo(event.screen)
                }
            }
        }

        return PlayerUiState(
            isLoading = isLoading,
            teamName = teamName,
            errorMessage = errorMessage,
            isShowPlayerCreationSection = isShowPlayerCreationSection,
            currentEditingPlayerId = currentEditingPlayerId,
            players = players,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(PlayerScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator, screen: PlayerScreen): PlayerPresenter
    }
}
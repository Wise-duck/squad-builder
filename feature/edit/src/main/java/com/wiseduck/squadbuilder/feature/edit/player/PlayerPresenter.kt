package com.wiseduck.squadbuilder.feature.edit.player

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.common.di.AdmobBannerId
import com.wiseduck.squadbuilder.core.common.utils.handleException
import com.wiseduck.squadbuilder.core.data.api.repository.PlayerRepository
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.feature.screens.PlayerScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class PlayerPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PlayerScreen,
    private val playerRepository: PlayerRepository,
    @AdmobBannerId private val admobBannerId: String,
) : Presenter<PlayerUiState> {

    @CircuitInject(PlayerScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            navigator: Navigator,
            screen: PlayerScreen,
        ): PlayerPresenter
    }

    @Composable
    override fun present(): PlayerUiState {
        val scope = rememberCoroutineScope()
        var sideEffect by remember { mutableStateOf<PlayerSideEffect?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        val teamName by remember { mutableStateOf(screen.teamName) }
        var isShowPlayerCreationSection by remember { mutableStateOf<Boolean>(false) }
        var currentEditingPlayerId by remember { mutableStateOf<Int?>(null) }
        var players by remember { mutableStateOf(persistentListOf<TeamPlayerModel>()) }

        LaunchedEffect(Unit) {
            playerRepository.getTeamPlayers(teamId = screen.teamId)
                .onSuccess {
                    players = it.toPersistentList()
                }
                .onFailure { exception ->
                    handleException(
                        exception = exception,
                        onError = { uiText ->
                            PlayerSideEffect.ShowToast(uiText)
                        },
                    )
                    Log.e("PlayerPresenter", "선수 목록 로드 실패: $exception")
                }
        }

        fun handleEvent(event: PlayerUiEvent) {
            when (event) {
                PlayerUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is PlayerUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is PlayerUiEvent.OnPlayerCreationClick -> {
                    isShowPlayerCreationSection = true
                }

                is PlayerUiEvent.OnPlayerCreationCancelClick -> {
                    isShowPlayerCreationSection = false
                }

                is PlayerUiEvent.OnPlayerCreationConfirmClick -> {
                    isLoading = true
                    scope.launch {
                        playerRepository.createTeamPlayer(
                            teamId = screen.teamId,
                            name = event.name,
                            position = event.position,
                            backNumber = event.backNumber,
                        )
                            .onSuccess {
                                isLoading = false
                                isShowPlayerCreationSection = false
                                players = players.add(it)
                            }
                            .onFailure { exception ->
                                isLoading = false
                                handleException(
                                    exception = exception,
                                    onError = { uiText ->
                                        sideEffect = PlayerSideEffect.ShowToast(uiText)
                                    },
                                )
                                Log.e("PlayerPresenter", "선수 생성 실패: $exception")
                            }
                    }
                }

                is PlayerUiEvent.OnPlayerDeleteClick -> {
                    isLoading = true
                    scope.launch {
                        playerRepository.deleteTeamPlayer(
                            teamId = screen.teamId,
                            playerId = event.playerId,
                        )
                            .onSuccess {
                                isLoading = false
                                players = players.mutate { list ->
                                    list.removeIf { it.id == event.playerId }
                                }
                                currentEditingPlayerId = null
                            }
                            .onFailure { exception ->
                                isLoading = false
                                handleException(
                                    exception = exception,
                                    onError = { uiText ->
                                        PlayerSideEffect.ShowToast(uiText)
                                    },
                                )
                                Log.e("PlayerPresenter", "선수 삭제 실패: $exception")
                            }
                    }
                }

                is PlayerUiEvent.OnPlayerEditClick -> {
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
                            backNumber = event.backNumber,
                        )
                            .onSuccess { updatedPlayer ->
                                isLoading = false
                                currentEditingPlayerId = null
                                players = players.mutate { list ->
                                    val index = list.indexOfFirst { it.id == updatedPlayer.id }
                                    if (index != -1) list[index] = updatedPlayer
                                }
                            }
                            .onFailure { exception ->
                                isLoading = false
                                currentEditingPlayerId = null
                                handleException(
                                    exception = exception,
                                    onError = { uiText ->
                                        sideEffect = PlayerSideEffect.ShowToast(uiText)
                                    },
                                )
                                Log.e("PlayerPresenter", "선수 수정 실패: $exception")
                            }
                    }
                }

                is PlayerUiEvent.OnPlayerUpdateCancel -> {
                    currentEditingPlayerId = null
                }

                is PlayerUiEvent.OnTabSelect -> {
                    navigator.goTo(event.screen)
                }
            }
        }

        return PlayerUiState(
            isLoading = isLoading,
            players = players,
            teamName = teamName,
            isShowPlayerCreationSection = isShowPlayerCreationSection,
            currentEditingPlayerId = currentEditingPlayerId,
            admobBannerId = admobBannerId,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

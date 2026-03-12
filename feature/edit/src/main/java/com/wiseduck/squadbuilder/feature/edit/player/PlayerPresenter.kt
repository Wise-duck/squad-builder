package com.wiseduck.squadbuilder.feature.edit.player

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
                    handleException(exception, onError = { sideEffect = PlayerSideEffect.ShowToast(it) })
                }
        }

        suspend fun createPlayer(teamId: Int, name: String, position: String, backNumber: Int) {
            playerRepository.createTeamPlayer(teamId, name, position, backNumber)
                .onSuccess {
                    isShowPlayerCreationSection = false
                    players = players.add(it)
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = PlayerSideEffect.ShowToast(it) })
                }
        }

        suspend fun deletePlayer(teamId: Int, playerId: Int) {
            playerRepository.deleteTeamPlayer(teamId, playerId)
                .onSuccess { players = players.mutate { list -> list.removeIf { it.id == playerId } }
                    currentEditingPlayerId = null
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = PlayerSideEffect.ShowToast(it) })
                }
        }

        suspend fun editPlayer(teamId: Int, name: String, position: String, backNumber: Int) {
            playerRepository.updateTeamPlayer(
                teamId = teamId,
                playerId = currentEditingPlayerId!!,
                name = name,
                position = position,
                backNumber = backNumber,
            )
                .onSuccess { updatedPlayer ->
                    currentEditingPlayerId = null
                    players = players.mutate { list ->
                        val index = list.indexOfFirst { it.id == updatedPlayer.id }
                        if (index != -1) list[index] = updatedPlayer
                    }
                }
                .onFailure { exception ->
                    currentEditingPlayerId = null
                    handleException(exception, onError = { sideEffect = PlayerSideEffect.ShowToast(it) })
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
                    scope.launch {
                        isLoading = true
                        createPlayer(screen.teamId, event.name, event.position, event.backNumber)
                        isLoading = false
                    }
                }

                is PlayerUiEvent.OnPlayerDeleteClick -> {
                    scope.launch {
                        isLoading = true
                        deletePlayer(screen.teamId, event.playerId)
                        isLoading = false
                    }
                }

                is PlayerUiEvent.OnPlayerEditClick -> {
                    currentEditingPlayerId = event.playerId
                }

                is PlayerUiEvent.OnPlayerUpdateConfirm -> {
                    scope.launch {
                        isLoading = true
                        editPlayer(screen.teamId, event.name, event.position, event.backNumber)
                        isLoading = false
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

package com.wiseduck.squadbuilder.feature.home

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
import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import com.wiseduck.squadbuilder.feature.screens.TeamDetailScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val teamRepository: TeamRepository
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(true) }
        var teams by remember { mutableStateOf(persistentListOf<TeamModel>()) }

        LaunchedEffect(Unit) {
            isLoading = true
            teamRepository.getTeams()
                .onSuccess { teamModels ->
                    isLoading = false
                    teams = teamModels.toImmutableList() as PersistentList<TeamModel>
                    Log.i("HomePresenter", "팀 목록 로드 성공: ${teams.size} teams")
                }
                .onFailure { error ->
                    isLoading = false
                    Log.e("HomePresenter", "팀 목록 로드 실패", error)
                }
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                is HomeUiEvent.OnTeamCardClick -> {
                    var clickedTeam = teams.find { it.teamId == event.teamId }
                    if (clickedTeam != null) {
                        navigator.goTo(TeamDetailScreen(clickedTeam))
                    }
                }

                is HomeUiEvent.OnTeamDeleteButtonClick -> {
                    scope.launch {
                        teamRepository.deleteTeam(event.teamId)
                            .onSuccess {
                                Log.d("HomePresenter", "팀(${event.teamId}) 삭제 성공. UI 업데이트")

                                val updatedTeams = teams.filter { it.teamId != event.teamId }

                                teams = updatedTeams.toImmutableList() as PersistentList<TeamModel>
                            }
                            .onFailure {
                                Log.e("HomePresenter", "팀(${event.teamId}) 삭제 실패", it)
                            }
                    }
                }

                is HomeUiEvent.OnTabSelect -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return HomeUiState(
            isLoading = isLoading,
            teams = teams,
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator) : HomePresenter
    }
}
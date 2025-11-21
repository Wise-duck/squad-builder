package com.wiseduck.squadbuilder.feature.home

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
        var isRefreshing by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var currentSortOption by remember { mutableStateOf(TeamSortOption.LATEST) }
        var teams by remember { mutableStateOf(persistentListOf<TeamModel>()) }
        val teamCreateErrorServerConnection = stringResource(R.string.team_create_error_server_connection)
        val updatedTeamListLoadFailed = stringResource(R.string.updated_team_list_load_failed)
        val loadFailedTeamList = stringResource(R.string.load_failed_team_list)

        fun sortTeams(teamModels: List<TeamModel>, sortOption: TeamSortOption): PersistentList<TeamModel> {
            val sortedList = when (sortOption) {
                TeamSortOption.LATEST -> teamModels.sortedByDescending { it.createdAt }
                TeamSortOption.NAME -> teamModels.sortedBy { it.name }
            }

            return sortedList.toImmutableList() as PersistentList<TeamModel>
        }

        fun loadTeams() {
            scope.launch {
                teamRepository.getTeams()
                    .onSuccess { teamModels ->
                        teams = sortTeams(teamModels, currentSortOption)
                        Log.i("HomePresenter", "팀 목록 로드 성공: ${teams.size} teams, 정렬 기준: $currentSortOption")
                    }
                    .onFailure { error ->
                        errorMessage = loadFailedTeamList
                        Log.e("HomePresenter", "팀 목록 로드 실패", error)
                    }
                isLoading = false
                isRefreshing = false
            }
        }

        LaunchedEffect(Unit) {
            isLoading = true
            loadTeams()
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                is HomeUiEvent.OnSortOptionSelect -> {
                    if (currentSortOption != event.sortOption) {
                        currentSortOption = event.sortOption
                        teams = sortTeams(teams, currentSortOption)
                        Log.d("HomePresenter", "정렬 기준 변경: $currentSortOption")
                    }
                }

                HomeUiEvent.OnRefresh -> {
                    if (!isRefreshing) {
                        isRefreshing = true
                        loadTeams()
                    }
                }

                is HomeUiEvent.OnTeamCreateButtonClick -> {
                    isLoading = true
                    scope.launch {
                        teamRepository.createTeam(event.teamName)
                            .onSuccess { teamModel ->
                                teamRepository.getTeams()
                                    .onSuccess { updatedTeamList ->
                                        isLoading = false
                                        teams = updatedTeamList.toImmutableList() as PersistentList<TeamModel>
                                        Log.d("HomePresenter", " ${teamModel.name}팀 생성 성공. UI 업데이트.")
                                    }
                                    .onFailure {
                                        isLoading = false
                                        errorMessage = updatedTeamListLoadFailed
                                        Log.e("HomePresenter", "팀 생성은 성공했지만, 목록 로드 실패", it)
                                    }
                            }
                            .onFailure { error ->
                                isLoading = false
                                errorMessage = teamCreateErrorServerConnection
                                Log.e("HomePresenter", "팀 생성 실패", error)
                            }
                    }
                }

                is HomeUiEvent.OnTeamCardClick -> {
                        navigator.goTo(TeamDetailScreen(
                            teamId = event.teamId,
                            teamName = event.teamName
                        ))
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

                is HomeUiEvent.OnDialogCloseButtonClick -> {
                    errorMessage = null
                }

                is HomeUiEvent.OnTabSelect -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return HomeUiState(
            isLoading = isLoading,
            isRefreshing = isRefreshing,
            currentSortOption = currentSortOption,
            errorMessage = errorMessage,
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
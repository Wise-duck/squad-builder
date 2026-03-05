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
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.common.di.AdmobBannerId
import com.wiseduck.squadbuilder.core.common.utils.handleException
import com.wiseduck.squadbuilder.core.data.api.repository.AuthRepository
import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.model.LoginState
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import com.wiseduck.squadbuilder.feature.screens.LoginScreen
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
    private val authRepository: AuthRepository,
    private val teamRepository: TeamRepository,
    @AdmobBannerId private val admobBannerId: String,
) : Presenter<HomeUiState> {

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        var sideEffect by remember { mutableStateOf<HomeSideEffect?>(null) }

        var isLoading by remember { mutableStateOf(true) }
        var isRefreshing by remember { mutableStateOf(false) }

        val loginState by authRepository.loginState.collectAsRetainedState(LoginState.NOT_YET)
        val isLoggedIn = loginState == LoginState.LOGGED_IN

        var currentSortOption by remember { mutableStateOf(TeamSortOption.LATEST) }
        var teams by remember { mutableStateOf(persistentListOf<TeamModel>()) }

        fun sortTeams(
            teamModels: List<TeamModel>,
            sortOption: TeamSortOption,
        ): PersistentList<TeamModel> {
            val sortedList =
                when (sortOption) {
                    TeamSortOption.LATEST -> teamModels.sortedByDescending { it.createdAt }
                    TeamSortOption.NAME -> teamModels.sortedBy { it.name }
                }

            return sortedList.toImmutableList() as PersistentList<TeamModel>
        }

        fun loadTeams() {
            scope.launch {
                if (!isLoggedIn) {
                    teams = persistentListOf()
                    isLoading = false
                    isRefreshing = false
                    return@launch
                }

                teamRepository.getTeams()
                    .onSuccess { teamModels ->
                        teams = sortTeams(teamModels, currentSortOption)
                    }
                    .onFailure { exception ->
                        handleException(
                            exception = exception,
                            onError = { uiText ->
                                sideEffect = HomeSideEffect.ShowToast(uiText)
                            },
                        )
                        Log.e("HomePresenter", "팀 목록 로드 실패", exception)
                    }
                isLoading = false
                isRefreshing = false
            }
        }

        LaunchedEffect(isLoggedIn) {
            isLoading = true
            loadTeams()
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                HomeUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is HomeUiEvent.OnSortOptionSelect -> {
                    if (currentSortOption != event.sortOption) {
                        currentSortOption = event.sortOption
                        teams = sortTeams(teams, currentSortOption)
                    }
                }

                HomeUiEvent.OnRefresh -> {
                    if (!isRefreshing) {
                        isRefreshing = true
                        loadTeams()
                    }
                }

                is HomeUiEvent.OnTeamCreateButtonClick -> {
                    if (!isLoggedIn) {
                        navigator.goTo(LoginScreen)
                        return
                    }

                    isLoading = true
                    scope.launch {
                        teamRepository.createTeam(event.teamName)
                            .onSuccess { teamModel ->
                                teamRepository.getTeams()
                                    .onSuccess { updatedTeamList ->
                                        isLoading = false
                                        teams = sortTeams(updatedTeamList, currentSortOption)
                                    }
                                    .onFailure { exception ->
                                        isLoading = false
                                        handleException(
                                            exception = exception,
                                            onError = { uiText ->
                                                sideEffect = HomeSideEffect.ShowToast(uiText)
                                            },
                                        )
                                        Log.e("HomePresenter", "팀 생성은 성공했지만, 목록 로드 실패", exception)
                                    }
                            }
                            .onFailure { exception ->
                                isLoading = false
                                handleException(
                                    exception = exception,
                                    onError = { uiText ->
                                        sideEffect = HomeSideEffect.ShowToast(uiText)
                                    },
                                )
                                Log.e("HomePresenter", "팀 생성 실패", exception)
                            }
                    }
                }

                is HomeUiEvent.OnTeamCardClick -> {
                    navigator.goTo(
                        TeamDetailScreen(
                            teamId = event.teamId,
                            teamName = event.teamName,
                        ),
                    )
                }

                is HomeUiEvent.OnTeamDeleteButtonClick -> {
                    scope.launch {
                        teamRepository.deleteTeam(event.teamId)
                            .onSuccess {
                                val updatedTeams = teams.filter { it.teamId != event.teamId }

                                teams = updatedTeams.toImmutableList() as PersistentList<TeamModel>
                            }
                            .onFailure { exception ->
                                handleException(
                                    exception = exception,
                                    onError = { uiText ->
                                        sideEffect = HomeSideEffect.ShowToast(uiText)
                                    },
                                )
                                Log.e("HomePresenter", "팀(${event.teamId}) 삭제 실패", exception)
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
            isRefreshing = isRefreshing,
            isLoggedIn = isLoggedIn,
            adUnitId = admobBannerId,
            currentSortOption = currentSortOption,
            teams = teams,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

package com.wiseduck.squadbuilder.feature.home

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
import com.wiseduck.squadbuilder.core.common.utils.UiText
import com.wiseduck.squadbuilder.core.common.utils.handleException
import com.wiseduck.squadbuilder.core.data.api.repository.AuthRepository
import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.model.LoginState
import com.wiseduck.squadbuilder.core.model.Team
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
        var teams by remember { mutableStateOf(persistentListOf<Team>()) }
        var teamToDelete by remember { mutableStateOf<Team?>(null) }

        fun sortTeams(
            teams: List<Team>,
            sortOption: TeamSortOption,
        ): PersistentList<Team> {
            val sortedList =
                when (sortOption) {
                    TeamSortOption.LATEST -> teams.sortedByDescending { it.createdAt }
                    TeamSortOption.NAME -> teams.sortedBy { it.name }
                }

            return sortedList.toImmutableList() as PersistentList<Team>
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
                        handleException(exception, onError = { sideEffect = HomeSideEffect.ShowToast(it) })
                    }
                isLoading = false
                isRefreshing = false
            }
        }

        suspend fun createTeam(teamName: String) {
            teamRepository.createTeam(teamName)
                .onSuccess { teamModel ->
                    teamRepository.getTeams()
                        .onSuccess { updatedTeamList ->
                            teams = sortTeams(updatedTeamList, currentSortOption)
                        }
                        .onFailure { exception ->
                            handleException(exception, onError = { sideEffect = HomeSideEffect.ShowToast(it) })
                        }
                }
                .onFailure { exception ->
                    handleException(exception, onError = { sideEffect = HomeSideEffect.ShowToast(it) })
                }
        }

        fun deleteTeam(teamId: Int) {
            scope.launch {
                teamRepository.deleteTeam(teamId)
                    .onSuccess {
                        val updatedTeams = teams.filter { it.teamId != teamId }
                        teams = updatedTeams.toImmutableList() as PersistentList<Team>
                    }
                    .onFailure { exception ->
                        handleException(exception, onError = { sideEffect = HomeSideEffect.ShowToast(it) })
                    }
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

                    if (teams.size >= 3) {
                        sideEffect = HomeSideEffect.ShowToast(UiText.StringResource(R.string.team_limit_reached))
                        return
                    }

                    scope.launch {
                        isLoading = true
                        createTeam(event.teamName)
                        isLoading = false
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

                is HomeUiEvent.OnTeamDeleteClick -> {
                    teamToDelete = event.team
                }

                is HomeUiEvent.OnTeamDeleteConfirm -> {
                    teamToDelete = null
                    deleteTeam(event.teamId)
                }

                is HomeUiEvent.OnTabSelect -> {
                    navigator.resetRoot(event.screen)
                }

                is HomeUiEvent.OnDismissTeamDeleteDialog -> {
                    teamToDelete = null
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
            teamToDelete = teamToDelete,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

package com.wiseduck.squadbuilder.feature.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.data.api.repository.TeamRepository
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val teamRepository: TeamRepository
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {

        var isLoading by remember { mutableStateOf(true) }
        var teams by remember { mutableStateOf(persistentListOf<TeamModel>()) }

        LaunchedEffect(Unit) {
            isLoading = true
            teamRepository.getTeams()
                .onSuccess { homeModel ->
                    teams = homeModel.teams.toImmutableList() as PersistentList<TeamModel>
                    isLoading = false
                    Log.i("HomePresenter", "팀 목록 로드 성공: ${homeModel.teams.size} teams")
                }
                .onFailure { error ->
                    isLoading = false
                    Log.e("HomePresenter", "팀 목록 로드 실패", error)
                }
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                is HomeUiEvent.OnMyTeamDetailClick -> {

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
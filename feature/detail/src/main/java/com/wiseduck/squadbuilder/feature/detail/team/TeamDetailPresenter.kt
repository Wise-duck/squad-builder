package com.wiseduck.squadbuilder.feature.detail.team

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.model.TeamModel
import com.wiseduck.squadbuilder.feature.screens.TeamDetailScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class TeamDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: TeamDetailScreen,
) : Presenter<TeamDetailUiState> {

    @Composable
    override fun present(): TeamDetailUiState {
        val teamId = screen.teamId
        val teamName = screen.teamName

        fun handleEvent(event: TeamDetailEvent) {
            when(event) {
                is TeamDetailEvent.OnManagePlayersClick -> {}
                is TeamDetailEvent.OnManageFormationClick -> {}
                is TeamDetailEvent.OnBackButtonClick -> {
                    navigator.pop()
                }
            }
        }

        return TeamDetailUiState(
            isLoading = false,
            team = TeamModel(
                teamId = teamId,
                name = teamName
            ),
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(TeamDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: TeamDetailScreen, navigator: Navigator): TeamDetailPresenter
    }
}

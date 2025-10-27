package com.wiseduck.squadbuilder.feature.home

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                is HomeUiEvent.OnMyTeamDetailClick -> {

                }
            }
        }

        return HomeUiState(
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator) : HomePresenter
    }
}
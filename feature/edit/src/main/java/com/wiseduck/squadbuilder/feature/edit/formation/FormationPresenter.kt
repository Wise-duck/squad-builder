package com.wiseduck.squadbuilder.feature.edit.formation

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class FormationPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: FormationScreen,
    ) : Presenter<FormationUiState> {
    @Composable
    override fun present(): FormationUiState {
        val teamId = screen.teamId
        val teamName = screen.teamName

        fun handleEvent(event: FormationUiEvent) {
            when (event) {
                FormationUiEvent.OnBackButtonClick -> navigator.pop()
                FormationUiEvent.OnFormationResetClick -> { /* TODO: 초기화 로직 */ }
                FormationUiEvent.OnFormationListClick -> { /* TODO: 목록 로직 */ }
                FormationUiEvent.OnFormationSaveClick -> { /* TODO: 저장 로직 */ }
            }
        }

        return FormationUiState(
            teamId = teamId,
            teamName = teamName,
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: FormationScreen, navigator: Navigator): FormationPresenter
    }
}
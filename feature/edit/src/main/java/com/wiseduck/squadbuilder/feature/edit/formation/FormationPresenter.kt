package com.wiseduck.squadbuilder.feature.edit.formation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.data.api.repository.FormationRepository
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class FormationPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: FormationScreen,
    private val formationRepository: FormationRepository
    ) : Presenter<FormationUiState> {
    @Composable
    override fun present(): FormationUiState {
        val teamId = screen.teamId
        val teamName = screen.teamName

        val scope = rememberCoroutineScope()
        var formationList by remember { mutableStateOf(emptyList<FormationListItemModel>()) }
        var isListModalVisible by remember { mutableStateOf(false) }


        fun handleEvent(event: FormationUiEvent) {
            when (event) {
                FormationUiEvent.OnBackButtonClick -> navigator.pop()
                FormationUiEvent.OnFormationResetClick -> { /* TODO: 초기화 로직 */ }
                FormationUiEvent.OnFormationListClick -> {
                    scope.launch {
                        formationRepository.getFormationList(teamId)
                            .onSuccess { list ->
                                formationList = list
                                isListModalVisible = true
                            }
                            .onFailure {  }

                    }
                }
                FormationUiEvent.OnFormationSaveClick -> { /* TODO: 저장 로직 */ }
                FormationUiEvent.OnDismissListModal -> {
                    isListModalVisible = false
                }
            }
        }

        return FormationUiState(
            teamId = teamId,
            teamName = teamName,
            formationList = formationList,
            isListModalVisible = isListModalVisible,
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: FormationScreen,
            navigator: Navigator
        ): FormationPresenter
    }
}
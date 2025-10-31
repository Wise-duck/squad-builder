package com.wiseduck.squadbuilder.feature.edit.formation

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.wiseduck.squadbuilder.core.model.FormationListItemModel

data class FormationUiState (
    val teamId: Int = 0,
    val teamName: String = "",
    val eventSink: (FormationUiEvent) -> Unit,
    val formationList: List<FormationListItemModel> = emptyList(),
    val isListModalVisible: Boolean = false,
) : CircuitUiState

sealed interface FormationUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : FormationUiEvent
    data object OnFormationResetClick : FormationUiEvent
    data object OnFormationListClick : FormationUiEvent
    data object OnFormationSaveClick : FormationUiEvent
    data object OnDismissListModal : FormationUiEvent
}

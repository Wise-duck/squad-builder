package com.wiseduck.squadbuilder.feature.edit.formation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.SoccerField
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationController
import com.wiseduck.squadbuilder.feature.edit.formation.component.FormationHeader
import com.wiseduck.squadbuilder.feature.screens.FormationScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(FormationScreen::class, ActivityRetainedComponent::class)
@Composable
fun FormationUi(
    state: FormationUiState,
    modifier: Modifier = Modifier,
) {
    SquadBuilderScaffold (
        modifier = modifier.fillMaxSize()
    ){ innerPadding ->
        Column (
            modifier = modifier.padding(innerPadding)
        ) {
            FormationHeader(
                modifier = modifier,
                onBackClick = {
                    state.eventSink(FormationUiEvent.OnBackButtonClick)
                }
            )

            FormationController(
                teamName = state.teamName,
                onFormationResetClick = { state.eventSink(FormationUiEvent.OnFormationResetClick) },
                onFormationListClick = { state.eventSink(FormationUiEvent.OnFormationListClick) },
                onFormationSaveClick = { state.eventSink(FormationUiEvent.OnFormationSaveClick) }

            )

            SoccerField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun FormationContent(
    modifier: Modifier = Modifier,
    state: FormationUiState
) {

}

@DevicePreview
@Composable
private fun FormationUiPreView() {
    SquadBuilderTheme {
        FormationUi(
            state = FormationUiState(
                teamId = 1,
                teamName = "비안코",
                eventSink = {}
            )
        )
    }
}
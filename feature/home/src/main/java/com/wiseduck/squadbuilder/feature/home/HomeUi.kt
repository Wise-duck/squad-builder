package com.wiseduck.squadbuilder.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.feature.home.component.HomeHeader
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    state: HomeUiState
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            HomeHeader(
                modifier = modifier
            )
//            if (state.isLoading) {
//                Box(
//                    modifier = modifier.fillMaxSize()
//                        .align(Alignment.CenterHorizontally)
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
        }
    }
}

@DevicePreview
@Composable
private fun HomeUi() {
    SquadBuilderTheme {
        HomeUi(
            state = HomeUiState(
                eventSink = {}
            )
        )
    }
}
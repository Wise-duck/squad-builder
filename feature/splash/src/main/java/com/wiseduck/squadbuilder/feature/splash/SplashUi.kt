package com.wiseduck.squadbuilder.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Black
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.feature.screens.SplashScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
@Composable
fun SplashUi(
    modifier: Modifier = Modifier,
    state: SplashUiState
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(SquadBuilderTheme.spacing.spacing8),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "App Logo",
            )
            Spacer(modifier = Modifier
                .height(SquadBuilderTheme.spacing.spacing12))
            Text(
                text = stringResource(R.string.splash_title),
                style = SquadBuilderTheme.typography.body1Bold,
                color = Green500
            )
        }

        if (state.isUpdateDialogVisible) {
            SquadBuilderDialog(
                onConfirmRequest = {
                    state.eventSink(SplashUiEvent.OnUpdateButtonClick)
                },
                confirmButtonText = stringResource(R.string.update_dialog_confirm_text_button),
                title = stringResource(R.string.update_dialog_title),
                description = stringResource(R.string.update_dialog_description),
            )
        }
    }
}

@DevicePreview
@Composable
fun SplashUiPreview() {
    SplashUi(
        state = SplashUiState(
            eventSink = {}
        )
    )
}
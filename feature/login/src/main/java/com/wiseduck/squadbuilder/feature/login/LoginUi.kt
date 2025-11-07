package com.wiseduck.squadbuilder.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.largeButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.feature.screens.LoginScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
@Composable
fun LoginUi(
    modifier: Modifier = Modifier,
    state: LoginUiState
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = com.wiseduck.squadbuilder.core.designsystem.R.drawable.ic_launcher_foreground),
                contentDescription = "SquadBuilder App Logo"
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )
            SquadBuilderButton(
                onClick = {
                    state.eventSink(LoginUiEvent.OnKakaoLoginButtonClick)
                },
                text = stringResource(R.string.login_button_kakao),
                sizeStyle = largeButtonStyle,
                colorStyle = ButtonColorStyle.KAKAO,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_kakao),
                        contentDescription = "KaKao Icon",
                        tint = Neutral800
                    )
                }
            )
        }

        if (state.isUpdateDialogVisible) {
            SquadBuilderDialog(
                onConfirmRequest = {
                    state.eventSink(LoginUiEvent.OnUpdateButtonClick)
                },
                confirmButtonText = stringResource(R.string.update_dialog_confirm_text_button),
                title = stringResource(R.string.update_dialog_title),
                description = stringResource(R.string.update_dialog_description),
            )
        }

        if (state.errorMessage != null) {
            SquadBuilderDialog(
                onConfirmRequest = {
                    state.eventSink(LoginUiEvent.OnCloseDialogButtonClick)
                },
                confirmButtonText = "확인",
                title = "오류 발생",
                description = state.errorMessage,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun LoginUi() {
    SquadBuilderTheme {
        LoginUi(
            state = LoginUiState(
                eventSink = {}
            )
        )
    }
}
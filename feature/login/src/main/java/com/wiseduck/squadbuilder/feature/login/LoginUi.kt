package com.wiseduck.squadbuilder.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.largeButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
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
                text = stringResource(R.string.kakao_login_text_button),
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
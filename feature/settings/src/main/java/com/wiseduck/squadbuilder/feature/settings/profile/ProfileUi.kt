package com.wiseduck.squadbuilder.feature.settings.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.largeButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.screens.ProfileScreen
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomBar
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomTab
import com.wiseduck.squadbuilder.feature.settings.profile.component.ProfileCard
import com.wiseduck.squadbuilder.feature.settings.profile.component.ProfileHeader
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(ProfileScreen::class, ActivityRetainedComponent::class)
@Composable
fun ProfileUi(
    modifier: Modifier = Modifier,
    state: ProfileUiState
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SquadBuilderBottomBar(
                modifier = modifier,
                currentTab = SquadBuilderBottomTab.PROFILE,
                onTabSelected = {
                    state.eventSink(ProfileUiEvent.OnTabSelect(it.screen))
                }
            )
        } 
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            ProfileHeader(
                modifier = modifier
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2)
            )
            ProfileContent(
                modifier = modifier,
                onLogoutClick = {
                    state.eventSink(ProfileUiEvent.OnLogoutButtonClick)
                },
                onWithDrawClick = {
                    state.eventSink(ProfileUiEvent.OnWithDrawButtonClick)
                },
                onPrivacyPolicyClick = {
                    state.eventSink(ProfileUiEvent.OnPrivacyPolicyButtonClick)
                },
            )
        }

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        }

        if (state.errorMessage != null) {
            SquadBuilderDialog(
                title = "오류 발생",
                description = state.errorMessage,
                onConfirmRequest = {
                    state.eventSink(ProfileUiEvent.OnDialogCloseButtonClick)
                },
                confirmButtonText = "확인"
            )
        }
    }
}

@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    onWithDrawClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileCard(
            modifier = Modifier.padding(SquadBuilderTheme.spacing.spacing4)
        )
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
        )
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            SquadBuilderButton(
                text = "로그아웃",
                onClick = onLogoutClick,
                sizeStyle = largeButtonStyle,
                colorStyle = ButtonColorStyle.TEXT
            )
            SquadBuilderButton(
                text = "계정 삭제",
                onClick = onWithDrawClick,
                sizeStyle = largeButtonStyle,
                colorStyle = ButtonColorStyle.TEXT
            )
            SquadBuilderButton(
                text = "개인정보처리방침",
                onClick = onPrivacyPolicyClick,
                sizeStyle = largeButtonStyle,
                colorStyle = ButtonColorStyle.TEXT
            )
        }
    }
}

@DevicePreview
@Composable
private fun ProfileUiPreview() {
    ProfileUi(
        state = ProfileUiState(
            isLoading = false,
            eventSink = {}
        )
    )
}
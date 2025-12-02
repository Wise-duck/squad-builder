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
import androidx.compose.ui.res.stringResource
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
import com.wiseduck.squadbuilder.feature.settings.R
import com.wiseduck.squadbuilder.feature.settings.profile.component.ProfileCard
import com.wiseduck.squadbuilder.feature.settings.profile.component.ProfileHeader
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(ProfileScreen::class, ActivityRetainedComponent::class)
@Composable
fun ProfileUi(
    modifier: Modifier = Modifier,
    state: ProfileUiState,
) {
    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SquadBuilderBottomBar(
                modifier = modifier,
                currentTab = SquadBuilderBottomTab.PROFILE,
                onTabSelected = {
                    state.eventSink(ProfileUiEvent.OnTabSelect(it.screen))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            ProfileHeader(
                modifier = modifier,
            )
            ProfileContent(
                modifier = modifier,
                userName = state.userName,
                isLoggedIn = state.isLoggedIn,
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
                title = stringResource(R.string.error_dialog_title),
                description = state.errorMessage,
                onConfirmRequest = {
                    state.eventSink(ProfileUiEvent.OnDialogCloseButtonClick)
                },
                confirmButtonText = stringResource(R.string.dialog_close_text_button),
            )
        }
    }
}

@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    userName: String,
    isLoggedIn: Boolean,
    onLogoutClick: () -> Unit,
    onWithDrawClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileCard(
            modifier = Modifier.padding(SquadBuilderTheme.spacing.spacing4),
            name = userName,
        )
        Spacer(
            modifier = Modifier.weight(1f),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4),
            horizontalAlignment = Alignment.Start,
        ) {
            if (isLoggedIn) {
                SquadBuilderButton(
                    text = stringResource(R.string.logout_button),
                    onClick = onLogoutClick,
                    sizeStyle = largeButtonStyle,
                    colorStyle = ButtonColorStyle.TEXT,
                )
                SquadBuilderButton(
                    text = stringResource(R.string.withdraw_button),
                    onClick = onWithDrawClick,
                    sizeStyle = largeButtonStyle,
                    colorStyle = ButtonColorStyle.TEXT,
                )
            }
            SquadBuilderButton(
                text = stringResource(R.string.privacy_policy_button),
                onClick = onPrivacyPolicyClick,
                sizeStyle = largeButtonStyle,
                colorStyle = ButtonColorStyle.TEXT,
            )
        }
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2),
        )
    }
}

@DevicePreview
@Composable
private fun ProfileUiPreview() {
    ProfileUi(
        state = ProfileUiState(
            isLoading = false,
            isLoggedIn = true,
            userName = "주름이",
            eventSink = {},
        ),
    )
}

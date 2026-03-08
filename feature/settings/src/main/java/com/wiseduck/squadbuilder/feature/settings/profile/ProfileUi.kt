package com.wiseduck.squadbuilder.feature.settings.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderLoadingIndicator
import com.wiseduck.squadbuilder.feature.screens.ProfileScreen
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomBar
import com.wiseduck.squadbuilder.feature.screens.component.SquadBuilderBottomTab
import com.wiseduck.squadbuilder.feature.settings.R
import com.wiseduck.squadbuilder.feature.settings.profile.component.ProfileCard
import com.wiseduck.squadbuilder.feature.settings.profile.component.ProfileHeader
import com.wiseduck.squadbuilder.feature.settings.profile.mock.profileUiStateMock
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList

@CircuitInject(ProfileScreen::class, ActivityRetainedComponent::class)
@Composable
fun ProfileUi(
    modifier: Modifier = Modifier,
    state: ProfileUiState,
) {
    HandleProfileSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

    SquadBuilderScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SquadBuilderBottomBar(
                tabs = SquadBuilderBottomTab.entries.toImmutableList(),
                currentTab = SquadBuilderBottomTab.PROFILE,
                onTabSelected = {
                    state.eventSink(ProfileUiEvent.OnTabSelect(it.screen))
                },
            )
        },
    ) { innerPadding ->
        ProfileContent(
            innerPadding = innerPadding,
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

        if (state.isLoading) {
            SquadBuilderLoadingIndicator()
        }
    }
}

@Composable
private fun ProfileContent(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    userName: String,
    isLoggedIn: Boolean,
    onLogoutClick: () -> Unit,
    onWithDrawClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
) {
    Column(
        modifier = modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileHeader()

        ProfileCard(
            modifier = Modifier.padding(SquadBuilderTheme.spacing.spacing4),
            name = userName,
        )
        Spacer(modifier = Modifier.weight(1f))

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
        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))
    }
}

@DevicePreview
@Composable
private fun ProfileUiPreview() {
    ProfileUi(
        state = profileUiStateMock,
    )
}

@DevicePreview
@Composable
private fun ProfileUiGuestPreview() {
    ProfileUi(
        state = profileUiStateMock.copy(
            isLoggedIn = false,
        ),
    )
}

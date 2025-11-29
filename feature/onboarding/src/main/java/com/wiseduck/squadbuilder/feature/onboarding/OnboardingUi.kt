package com.wiseduck.squadbuilder.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.mediumRoundedButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.onboarding.component.OnboardingPage
import com.wiseduck.squadbuilder.feature.onboarding.component.PageController
import com.wiseduck.squadbuilder.feature.screens.OnboardingScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
@Composable
fun OnboardingUi(
    modifier: Modifier = Modifier,
    state: OnboardingUiState,
) {
    Column(
        modifier = modifier
            .background(MainBg)
            .padding(SquadBuilderTheme.spacing.spacing8),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = state.pagerState,
            modifier = Modifier.weight(1f),
        ) {
            when (state.pagerState.currentPage) {
                0 ->
                    OnboardingPage(
                        modifier = modifier,
                        image = painterResource(R.drawable.ic_onboarding_page_1_image),
                        description = stringResource(R.string.onboarding_page_1_description),
                    )
                1 ->
                    OnboardingPage(
                        modifier = modifier,
                        image = painterResource(R.drawable.ic_onboarding_page_2_image),
                        description = stringResource(R.string.onboarding_page_2_description),
                    )
                2 ->
                    OnboardingPage(
                        modifier = modifier,
                        image = painterResource(R.drawable.ic_onboarding_page_3_image),
                        description = stringResource(R.string.onboarding_page_3_description),
                    )
            }
        }
        PageController(
            modifier = modifier,
            stepCount = ONBOARDING_STEPS,
            pagerState = state.pagerState,
        )
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing12),
        )
        SquadBuilderButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SquadBuilderTheme.spacing.spacing8),
            text = stringResource(R.string.onboarding_next_button),
            onClick = {
                state.eventSink(OnboardingUiEvent.OnNextButtonClick(state.pagerState.currentPage))
            },
            colorStyle = ButtonColorStyle.STROKE,
            sizeStyle = mediumRoundedButtonStyle,
        )
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing12),
        )
    }
}

@DevicePreview
@Composable
private fun OnboardingUiPreview() {
    val pagerState = rememberPagerState(
        pageCount = {
            ONBOARDING_STEPS
        },
    )

    SquadBuilderTheme {
        OnboardingUi(
            state = OnboardingUiState(
                pagerState = pagerState,
                eventSink = {},
            ),
        )
    }
}

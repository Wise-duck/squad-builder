package com.wiseduck.squadbuilder.feature.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.onboarding.ONBOARDING_STEPS

@Composable
fun PageController(
    modifier: Modifier = Modifier,
    stepCount: Int,
    pagerState: PagerState
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        repeat(stepCount) {
            val color = if (pagerState.currentPage == it) Green500 else Neutral500
            Box(
                modifier = Modifier
                    .background(color)
            )
        }
    }
}

@Composable
fun PageControllerPreview() {
    val pagerState = rememberPagerState(
        pageCount = { ONBOARDING_STEPS }
    )

    SquadBuilderTheme {
        PageController(
            stepCount = ONBOARDING_STEPS,
            pagerState = pagerState,
        )
    }
}
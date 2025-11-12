package com.wiseduck.squadbuilder.feature.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.onboarding.R

@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    image: Painter,
    description: String
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image,
            contentDescription = "Onboarding Image"
        )
        Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))
        Text(
            text = description,
            style = SquadBuilderTheme.typography.body1Bold
        )
    }
}

@Composable
private fun OnboardingPagePreview() {
    SquadBuilderTheme {
        OnboardingPage(
            image = painterResource(R.drawable.ic_onboarding_page_1_image),
            description = stringResource(R.string.onboarding_page_1_description)
        )
    }
}
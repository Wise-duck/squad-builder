package com.wiseduck.squadbuilder.feature.settings.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.feature.settings.R

@Composable
fun ProfileHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MainBg),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4),
        ) {
            Spacer(
                modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4),
            )
            Text(
                text = stringResource(R.string.profile_screen_header_title),
                style = SquadBuilderTheme.typography.title1Bold,
                color = White,
            )
        }
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(SquadBuilderTheme.spacing.spacing05)
                .background(Neutral800),
        )
    }
}

@ComponentPreview
@Composable
private fun ProfileHeaderPreview() {
    ProfileHeader()
}

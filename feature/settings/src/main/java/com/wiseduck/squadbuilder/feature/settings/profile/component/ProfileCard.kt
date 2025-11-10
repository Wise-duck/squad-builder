package com.wiseduck.squadbuilder.feature.settings.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.feature.settings.R
import com.wiseduck.squadbuilder.core.designsystem.theme.MainComponentBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.Yellow300

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    name: String
) {
    Card(
        modifier = modifier.fillMaxWidth()
            .padding(SquadBuilderTheme.spacing.spacing4),
        colors = CardDefaults.cardColors(
            MainComponentBg
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral500
        )
    ) {
        Column(
            Modifier.padding(SquadBuilderTheme.spacing.spacing6)
        ) {
            Text(
                text = stringResource(R.string.profile_card_title),
                color = Neutral100,
                style = SquadBuilderTheme.typography.heading1Bold
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )
            Row {
                Text(
                    stringResource(R.string.profile_card_name_label),
                    color = Neutral50
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = name,
                    color = Green500
                )
            }
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )
            Row {
                Text(
                    text = stringResource(R.string.profile_card_login_type_label),
                    color = Neutral50
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "카카오 로그인",
                    color = Yellow300
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ProfileCardPreview() {
    ProfileCard(
        name = "주름이"
    )
}
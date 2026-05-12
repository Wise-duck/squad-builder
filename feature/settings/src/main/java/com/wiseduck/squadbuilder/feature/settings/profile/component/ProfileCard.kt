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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.settings.R

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    name: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(SquadBuilderTheme.spacing.spacing4),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Column(
            Modifier.padding(SquadBuilderTheme.spacing.spacing6),
        ) {
            Text(
                text = stringResource(R.string.profile_card_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = SquadBuilderTheme.typography.heading1Bold,
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4),
            )
            Row {
                Text(
                    stringResource(R.string.profile_card_name_label),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4),
            )
            Row {
                Text(
                    text = stringResource(R.string.profile_card_login_type_label),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(R.string.profile_card_login_type_kakao),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ProfileCardPreview() {
    ProfileCard(
        name = "주름이",
    )
}

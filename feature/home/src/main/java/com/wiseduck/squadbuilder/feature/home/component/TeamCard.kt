package com.wiseduck.squadbuilder.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.common.extensions.DateFormats
import com.wiseduck.squadbuilder.core.common.extensions.toFormattedDate
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.Team
import com.wiseduck.squadbuilder.feature.home.R
import com.wiseduck.squadbuilder.feature.home.mock.fakeTeam

@Composable
fun TeamCard(
    modifier: Modifier = Modifier,
    team: Team,
    onDeleteClick: (Team) -> Unit,
    onClick: (Int, String) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(team.teamId, team.name) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SquadBuilderTheme.spacing.spacing4,
                    vertical = SquadBuilderTheme.spacing.spacing2,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = team.name,
                    style = SquadBuilderTheme.typography.heading1Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing1))
                Text(
                    text = stringResource(
                        R.string.creation_date_label,
                        team.createdAt.toFormattedDate(DateFormats.YY_MM_DD_DASH),
                    ),
                    style = SquadBuilderTheme.typography.label1Medium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            IconButton(
                onClick = { onDeleteClick(team) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Bin Icon",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun TeamCardPreview() {
    SquadBuilderTheme {
        TeamCard(
            team = fakeTeam,
            onDeleteClick = {},
            onClick = { _, _ -> },
        )
    }
}

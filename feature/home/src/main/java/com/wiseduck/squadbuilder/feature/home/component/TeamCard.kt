package com.wiseduck.squadbuilder.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.common.extensions.DateFormats
import com.wiseduck.squadbuilder.core.common.extensions.toFormattedDate
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Black
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamModel

@Composable
fun TeamCard(
    modifier: Modifier = Modifier,
    team: TeamModel,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Black
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral500
        )
    ) {
        Column(
            modifier = Modifier.padding(SquadBuilderTheme.spacing.spacing4)
        ) {
            Text(
                text = team.name,
                style = SquadBuilderTheme.typography.heading1Bold,
                color = Neutral100
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "생성일: ${team.createdAt.toFormattedDate(DateFormats.YY_MM_DD_DASH)}",
                style = SquadBuilderTheme.typography.label1Medium,
                color = Neutral500
            )
        }
    }
}

@ComponentPreview
@Composable
private fun TeamCardPreview() {
    SquadBuilderTheme {
        TeamCard(
            team = TeamModel(
                teamId = 1,
                name = "Team1",
                ownerId = "1",
                ownerEmail = "1@.com",
                createdAt = "2025-10-28T14:25:27.097Z"
            )
        )
    }
}
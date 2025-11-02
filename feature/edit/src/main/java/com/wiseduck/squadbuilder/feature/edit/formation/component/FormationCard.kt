package com.wiseduck.squadbuilder.feature.edit.formation.component

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.common.extensions.DateFormats
import com.wiseduck.squadbuilder.core.common.extensions.toFormattedDate
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Black
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.FormationListItemModel
import com.wiseduck.squadbuilder.feature.edit.R

@Composable
fun FormationCard(
    modifier: Modifier = Modifier,
    formation: FormationListItemModel,
    onDeleteClick: (Int) -> Unit,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(formation.formationId) },
        colors = CardDefaults.cardColors(
            containerColor = Black
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral500
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formation.name,
                    style = SquadBuilderTheme.typography.heading1Bold,
                    color = Neutral100
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "생성일: ${formation.createdAt.toFormattedDate(DateFormats.YY_MM_DD_DASH)}",
                    style = SquadBuilderTheme.typography.label1Medium,
                    color = Neutral500
                )
            }
            IconButton(
                onClick = { onDeleteClick(formation.formationId) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bin),
                    contentDescription = "Bin Icon",
                    tint = Red500
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun FormationCardPreview() {
    SquadBuilderTheme {
        FormationCard(
            formation = FormationListItemModel(
                formationId = 1,
                name = "테스트 포매이션",
                createdAt = "2025-10-28T14:25:27.097Z"
            ),
            onDeleteClick = {},
            onClick = {}
        )
    }
}
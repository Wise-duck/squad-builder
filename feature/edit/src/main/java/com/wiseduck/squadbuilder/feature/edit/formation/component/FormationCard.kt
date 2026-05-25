package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.wiseduck.squadbuilder.core.model.FormationListItem
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.feature.edit.formation.mock.fakeFormationListItem

@Composable
fun FormationCard(
    modifier: Modifier = Modifier,
    formation: FormationListItem,
    onDeleteClick: (Int) -> Unit,
    onClick: (Int) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(formation.formationId) },
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
                .padding(vertical = 4.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = formation.name,
                    style = SquadBuilderTheme.typography.body1Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(
                        R.string.formation_created_at_label,
                        formation.createdAt.toFormattedDate(DateFormats.YY_MM_DD_DASH),
                    ),
                    style = SquadBuilderTheme.typography.label1Medium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            IconButton(
                onClick = { onDeleteClick(formation.formationId) },
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(com.wiseduck.squadbuilder.core.designsystem.R.drawable.ic_remove),
                    contentDescription = "Remove Icon",
                    tint = MaterialTheme.colorScheme.error,
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
            formation = fakeFormationListItem,
            onDeleteClick = {},
            onClick = {},
        )
    }
}

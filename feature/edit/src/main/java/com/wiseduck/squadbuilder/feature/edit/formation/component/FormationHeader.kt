package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.edit.R

@Composable
fun FormationHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFormationListClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = modifier,
                onClick = onBackClick,
            ) {
                Icon(
                    painter = painterResource(com.wiseduck.squadbuilder.core.ui.R.drawable.ic_back),
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(
                modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4),
            )
            Text(
                text = stringResource(com.wiseduck.squadbuilder.feature.edit.R.string.formation_management_title),
                style = SquadBuilderTheme.typography.title1Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onFormationListClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drawer),
                    contentDescription = "List Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Spacer(
            modifier = modifier.fillMaxWidth()
                .height(SquadBuilderTheme.spacing.spacing05)
                .background(MaterialTheme.colorScheme.outline),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormationHeaderPreview() {
    SquadBuilderTheme {
        FormationHeader()
    }
}

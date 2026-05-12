package com.wiseduck.squadbuilder.feature.edit.player.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.R

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4))
            Text(
                stringResource(com.wiseduck.squadbuilder.feature.edit.R.string.player_management_screen_header_title),
                style = SquadBuilderTheme.typography.title1Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onAddClick,
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(com.wiseduck.squadbuilder.core.designsystem.R.drawable.ic_add),
                    contentDescription = "Add Icon",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Spacer(
            modifier = Modifier.fillMaxWidth()
                .height(SquadBuilderTheme.spacing.spacing05)
                .background(MaterialTheme.colorScheme.outline),
        )
    }
}

@ComponentPreview
@Composable
private fun PlayerHeaderPreview() {
    SquadBuilderTheme {
        PlayerHeader(
            onBackClick = {},
            onAddClick = {},
        )
    }
}

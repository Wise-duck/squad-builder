package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue500
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.core.designsystem.theme.Yellow300

@Composable
fun FormationController(
    modifier: Modifier = Modifier,
    teamName: String,
    formationName: String,
    onFormationResetClick: () -> Unit = {},
    onFormationShareClick: () -> Unit = {},
    onFormationSaveClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MainBg)
            .padding(
                top = SquadBuilderTheme.spacing.spacing4,
                bottom = SquadBuilderTheme.spacing.spacing2,
                start = SquadBuilderTheme.spacing.spacing2,
                end = SquadBuilderTheme.spacing.spacing2
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = R.drawable.ic_formation_team),
            contentDescription = "Team Icon",
            tint = Yellow300
        )

        Text(
            text = teamName,
            style = SquadBuilderTheme.typography.title1Bold,
            modifier = Modifier.padding(start = 12.dp),
            color = White
        )
        Text(
            text = formationName,
            style = SquadBuilderTheme.typography.body1Regular,
            modifier = Modifier.padding(start = 12.dp),
            color = White
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onFormationResetClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reset),
                contentDescription = "Reset Icon",
                tint = Red500
            )
        }

        IconButton(onClick = onFormationShareClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "Share Icon",
                tint = Blue500
            )
        }

        IconButton(onClick = onFormationSaveClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = "Save Icon",
                tint = Green500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormationControllerPreview() {
    SquadBuilderTheme {
        FormationController(
            teamName = "비안코",
            formationName = "4-4-2"
        )
    }
}
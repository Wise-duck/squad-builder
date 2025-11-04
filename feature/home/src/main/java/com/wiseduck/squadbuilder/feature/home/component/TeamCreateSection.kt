package com.wiseduck.squadbuilder.feature.home.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.mediumButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral700
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.feature.home.R

@Composable
fun TeamCreateSection(
    modifier: Modifier = Modifier,
    onTeamCreateClick: (String) -> Unit
) {
    var teamNameInput by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .padding(SquadBuilderTheme.spacing.spacing4)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(1f),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = White,
            ),
            value = teamNameInput,
            onValueChange = { teamNameInput = it },
            label = {
                Text(
                    text = stringResource(R.string.team_name_input_hint),
                    style = SquadBuilderTheme.typography.caption1Regular,
                    color = Neutral700
                )
            }
        )
        Spacer(
            modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4)
        )
        SquadBuilderButton(
            onClick = { onTeamCreateClick(teamNameInput) },
            text = "팀 생성",
            sizeStyle = mediumButtonStyle,
            colorStyle = ButtonColorStyle.STROKE
        )
    }
}

@ComponentPreview
@Composable
private fun TeamCreateSectionPreview() {
    SquadBuilderTheme {
        TeamCreateSection(
            onTeamCreateClick = {}
        )
    }
}
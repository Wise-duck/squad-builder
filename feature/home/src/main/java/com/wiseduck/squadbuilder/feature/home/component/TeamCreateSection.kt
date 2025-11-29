package com.wiseduck.squadbuilder.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.mediumButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.home.R

@Composable
fun TeamCreateSection(
    modifier: Modifier = Modifier,
    onTeamCreateClick: (String) -> Unit,
) {
    var teamNameInput by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SquadBuilderTheme.spacing.spacing4),
    ) {
        Text(
            text = stringResource(R.string.team_create_section_title),
            style = SquadBuilderTheme.typography.heading1SemiBold,
            color = Neutral50,
        )
        Spacer(
            modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = teamNameInput,
                onValueChange = { teamNameInput = it },
                shape = RoundedCornerShape(
                    SquadBuilderTheme.radius.md,
                ),
                placeholder = {
                    Text(stringResource(R.string.team_name_input_hint))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = Neutral500,
                    focusedPlaceholderColor = Neutral500,
                    unfocusedContainerColor = Neutral100,
                    focusedContainerColor = Neutral100,
                    focusedBorderColor = Blue500,
                ),
                singleLine = true,
            )
            Spacer(
                modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4),
            )
            SquadBuilderButton(
                onClick = { onTeamCreateClick(teamNameInput) },
                text = stringResource(R.string.team_create_text_button),
                sizeStyle = mediumButtonStyle,
                colorStyle = ButtonColorStyle.STROKE,
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun TeamCreateSectionPreview() {
    SquadBuilderTheme {
        TeamCreateSection(
            onTeamCreateClick = {},
        )
    }
}

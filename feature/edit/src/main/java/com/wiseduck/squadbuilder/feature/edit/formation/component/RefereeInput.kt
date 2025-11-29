package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Black
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.edit.R

@Composable
fun RefereeInput(
    modifier: Modifier = Modifier,
    currentQuarter: Int,
    currentRefereeName: String,
    onRefereeNameChange: (String) -> Unit = {},
) {
    val textFieldState = rememberTextFieldState(initialText = currentRefereeName)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(currentRefereeName) {
        if (textFieldState.text.toString() != currentRefereeName) {
            textFieldState.setTextAndPlaceCursorAtEnd(currentRefereeName)
        }
    }

    LaunchedEffect(textFieldState.text) {
        if (textFieldState.text.toString() != currentRefereeName) {
            onRefereeNameChange(textFieldState.text.toString())
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(35.dp)
            .background(
                color = Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(
                    size = SquadBuilderTheme.spacing.spacing16,
                ),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = SquadBuilderTheme.spacing.spacing2,
                    end = SquadBuilderTheme.spacing.spacing1,
                ),
            text = stringResource(
                R.string.quarter_referee_label,
                currentQuarter,
            ),
            color = Red500,
            style = SquadBuilderTheme.typography.caption1Regular,
        )
        Spacer(
            modifier = Modifier
                .width(SquadBuilderTheme.spacing.spacing05),
        )
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = SquadBuilderTheme.spacing.spacing2)
                .focusRequester(focusRequester),
            state = textFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
            textStyle = SquadBuilderTheme.typography.caption1Regular.copy(
                color = Green500,
            ),
            cursorBrush = SolidColor(Neutral50),
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (textFieldState.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.referee_input_placeholder),
                            color = Neutral300,
                            style = SquadBuilderTheme.typography.caption1Regular,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

@Composable
@ComponentPreview
private fun RefereeInputPreview() {
    SquadBuilderTheme {
        RefereeInput(
            currentQuarter = 1,
            currentRefereeName = "심판 이름",
        )
    }
}

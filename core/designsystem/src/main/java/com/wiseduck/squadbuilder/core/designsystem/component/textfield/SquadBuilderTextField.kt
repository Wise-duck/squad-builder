package com.wiseduck.squadbuilder.core.designsystem.component.textfield

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@Composable
fun SquadBuilderTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = RoundedCornerShape(
            SquadBuilderTheme.radius.md,
        ),
        placeholder = {
            Text(
                text = placeholder,
            )
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedPlaceholderColor = Neutral500,
            focusedPlaceholderColor = Neutral500,
            unfocusedContainerColor = Neutral100,
            focusedContainerColor = Neutral100,
            focusedBorderColor = Blue500,
        ),
    )
}

@ComponentPreview
@Composable
fun SquadBuilderTextFieldPreview() {
    SquadBuilderTheme {
        SquadBuilderTextField(
            value = "",
            placeholder = "이름 입력",
            onValueChange = {},
        )
    }
}

package com.wiseduck.squadbuilder.core.designsystem.component.textfield

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
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
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
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

package com.wiseduck.squadbuilder.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg

@Composable
fun SquadBuilderTextButton(
    onClick: () -> Unit,
    text: String,
    sizeStyle: ButtonSizeStyle,
    colorStyle: ButtonColorStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TextButton(
        onClick = {
            onClick()
        },
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            containerColor = colorStyle.containerColor(isPressed),
            contentColor = colorStyle.contentColor(),
            disabledContentColor = colorStyle.disabledContentColor(),
            disabledContainerColor = colorStyle.disabledContainerColor(),
        ),
        contentPadding = sizeStyle.paddingValues,
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = text,
                style = sizeStyle.textStyle.copy(
                    color = if (enabled) colorStyle.contentColor() else colorStyle.disabledContentColor(),
                ),
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = if (enabled) colorStyle.contentColor() else Color.Transparent,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ReedTextButtonPreview() {
    Row(
        modifier = Modifier.padding(20.dp)
            .background(MainBg),
        horizontalArrangement = Arrangement.Center
    ) {
        SquadBuilderTextButton(
            onClick = {},
            colorStyle = ButtonColorStyle.TEXT,
            sizeStyle = largeButtonStyle,
            text = "text button",
        )
        SquadBuilderTextButton(
            onClick = {},
            enabled = false,
            colorStyle = ButtonColorStyle.TEXT,
            sizeStyle = largeButtonStyle,
            text = "text button",
        )
    }
}

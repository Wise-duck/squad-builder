package com.wiseduck.squadbuilder.core.designsystem.component.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg

@Composable
fun SquadBuilderButton(
    onClick: () -> Unit,
    text: String,
    sizeStyle: ButtonSizeStyle,
    colorStyle: ButtonColorStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "Scale Animation",
    )

    Button(
        onClick = {
            onClick()
        },
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        enabled = enabled,
        shape = RoundedCornerShape(sizeStyle.radius),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorStyle.containerColor(isPressed),
            contentColor = colorStyle.contentColor(),
            disabledContentColor = colorStyle.disabledContentColor(),
            disabledContainerColor = colorStyle.disabledContainerColor(),
        ),
        border = colorStyle.borderStroke(),
        contentPadding = sizeStyle.paddingValues,
        interactionSource = interactionSource,
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier.size(sizeStyle.iconSize),
                contentAlignment = Alignment.Center,
            ) {
                leadingIcon()
            }
        }

        if (leadingIcon != null && text.isNotEmpty()) {
            Spacer(Modifier.width(sizeStyle.iconSpacing))
        }

        Text(
            text = text,
            style = sizeStyle.textStyle.copy(
                color = if (enabled) colorStyle.contentColor() else colorStyle.disabledContentColor(),
            ),
        )

        if (trailingIcon != null && text.isNotEmpty()) {
            Spacer(Modifier.width(sizeStyle.iconSpacing))
        }

        if (trailingIcon != null) {
            Box(
                modifier = Modifier.size(sizeStyle.iconSize),
                contentAlignment = Alignment.Center,
            ) {
                trailingIcon()
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ReedLargeButtonPreview() {
    Column(
        modifier = Modifier.padding(20.dp)
            .background(MainBg),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        SquadBuilderButton(
            onClick = {},
            colorStyle = ButtonColorStyle.STROKE,
            sizeStyle = largeButtonStyle,
            text = "button",
        )
        SquadBuilderButton(
            onClick = {},
            text = "button",
            colorStyle = ButtonColorStyle.STROKE,
            sizeStyle = largeRoundedButtonStyle,
        )
    }
}
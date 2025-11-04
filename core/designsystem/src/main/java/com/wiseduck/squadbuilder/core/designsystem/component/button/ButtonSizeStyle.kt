package com.wiseduck.squadbuilder.core.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

data class ButtonSizeStyle(
    val paddingValues: PaddingValues,
    val radius: Dp = 0.dp,
    val textStyle: TextStyle,
    val iconSpacing: Dp = 0.dp,
    val iconSize: Dp = 24.dp,
)

val smallButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = SquadBuilderTheme.spacing.spacing3,
            vertical = SquadBuilderTheme.spacing.spacing2,
        ),
        radius = SquadBuilderTheme.radius.xs,
        textStyle = SquadBuilderTheme.typography.label1Medium,
        iconSpacing = SquadBuilderTheme.spacing.spacing1,
        iconSize = 18.dp,
    )

val mediumButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = SquadBuilderTheme.spacing.spacing4,
            vertical = SquadBuilderTheme.spacing.spacing3,
        ),
        radius = SquadBuilderTheme.radius.sm,
        textStyle = SquadBuilderTheme.typography.label1Medium,
        iconSpacing = SquadBuilderTheme.spacing.spacing1,
        iconSize = 22.dp,
    )

val largeButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = SquadBuilderTheme.spacing.spacing5,
            vertical = 14.dp,
        ),
        radius = SquadBuilderTheme.radius.sm,
        textStyle = SquadBuilderTheme.typography.body1Medium,
        iconSpacing = SquadBuilderTheme.spacing.spacing2,
        iconSize = 24.dp,
    )

val smallRoundedButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = SquadBuilderTheme.spacing.spacing3,
            vertical = SquadBuilderTheme.spacing.spacing2,
        ),
        radius = SquadBuilderTheme.radius.full,
        textStyle = SquadBuilderTheme.typography.label1Medium,
        iconSpacing = SquadBuilderTheme.spacing.spacing1,
        iconSize = 18.dp,
    )

val mediumRoundedButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = SquadBuilderTheme.spacing.spacing4,
            vertical = SquadBuilderTheme.spacing.spacing3,
        ),
        radius = SquadBuilderTheme.radius.full,
        textStyle = SquadBuilderTheme.typography.label1Medium,
        iconSpacing = SquadBuilderTheme.spacing.spacing1,
        iconSize = 22.dp,
    )

val largeRoundedButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = SquadBuilderTheme.spacing.spacing5,
            vertical = SquadBuilderTheme.spacing.spacing3,
        ),
        radius = SquadBuilderTheme.radius.full,
        textStyle = SquadBuilderTheme.typography.body1Medium,
        iconSpacing = SquadBuilderTheme.spacing.spacing2,
        iconSize = 24.dp,
    )

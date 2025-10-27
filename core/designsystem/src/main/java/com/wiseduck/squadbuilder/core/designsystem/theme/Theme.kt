package com.wiseduck.squadbuilder.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalColorScheme = staticCompositionLocalOf { SquadBuilderColorScheme() }
private val LocalTypography = staticCompositionLocalOf { SquadBuilderTypography() }
private val LocalSpacing = staticCompositionLocalOf { SquadBuilderSpacing() }
private val LocalRadius = staticCompositionLocalOf { SquadBuilderRadius() }
private val LocalBorder = staticCompositionLocalOf { SquadBuilderBorder() }

@Composable
fun SquadBuilderTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        content = content,
    )
}

object SquadBuilderTheme {
    val colors: SquadBuilderColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: SquadBuilderTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val spacing: SquadBuilderSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val radius: SquadBuilderRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalRadius.current

    val border: SquadBuilderBorder
        @Composable
        @ReadOnlyComposable
        get() = LocalBorder.current
}

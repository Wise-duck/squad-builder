package com.wiseduck.squadbuilder.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LocalTypography = staticCompositionLocalOf { SquadBuilderTypography() }
private val LocalSpacing = staticCompositionLocalOf { SquadBuilderSpacing() }
private val LocalRadius = staticCompositionLocalOf { SquadBuilderRadius() }
private val LocalBorder = staticCompositionLocalOf { SquadBuilderBorder() }

private val DarkColors = darkColorScheme(
    primary = Green500,
    onPrimary = Color.White,
    primaryContainer = Green500.copy(alpha = 0.2f),
    onPrimaryContainer = Green500,

    secondary = Blue500,
    onSecondary = Color.White,

    tertiary = Yellow300,
    onTertiary = Color.Black,
    tertiaryContainer = Yellow300.copy(alpha = 0.2f),
    onTertiaryContainer = Yellow300,

    background = Neutral950,
    onBackground = Neutral50,

    surface = Neutral900,
    onSurface = Neutral50,

    surfaceVariant = Neutral800,
    onSurfaceVariant = Neutral300,

    outline = Neutral700,
    error = Red500,
    onError = Color.White,
)

private val LightColors = lightColorScheme(
    primary = Green500,
    onPrimary = Color.White,
    primaryContainer = Green500.copy(alpha = 0.1f),
    onPrimaryContainer = Green500,

    secondary = Blue600,
    onSecondary = Color.White,

    tertiary = Yellow300,
    onTertiary = Color.Black,
    tertiaryContainer = Yellow300.copy(alpha = 0.2f),
    onTertiaryContainer = Yellow300,

    background = Color.White,
    onBackground = Neutral900,

    surface = Neutral50,
    onSurface = Neutral900,

    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral600,

    outline = Neutral200,
    error = Red500,
    onError = Color.White,
)

@Composable
fun SquadBuilderTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (isDarkMode) DarkColors else LightColors

    CompositionLocalProvider {
        MaterialTheme(
            colorScheme = colors,
            content = content,
        )
    }
}

object SquadBuilderTheme {
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

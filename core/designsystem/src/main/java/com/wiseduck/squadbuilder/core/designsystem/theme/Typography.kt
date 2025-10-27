package com.wiseduck.squadbuilder.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.wiseduck.squadbuilder.core.designsystem.R

val bmjuaFamily = FontFamily(
    Font(R.font.bmjua, FontWeight.Medium, FontStyle.Normal),
)

val pretendardFamily = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
)

private val defaultLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None,
)

private val baseTextStyle = TextStyle(
    fontFamily = pretendardFamily,
    lineHeightStyle = defaultLineHeightStyle,
    color = Neutral800,
)

private fun style(
    fontSize: Int,
    lineHeight: Int,
    letterSpacing: Float,
    fontWeight: FontWeight,
) = baseTextStyle.copy(
    fontSize = fontSize.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing.sp,
    fontWeight = fontWeight,
)

@Immutable
data class SquadBuilderTypography(
    val title1Bold: TextStyle = style(28, 38, -0.66f, FontWeight.Bold),
    val title1SemiBold: TextStyle = style(28, 38, -0.66f, FontWeight.SemiBold),
    val title1Medium: TextStyle = style(28, 38, -0.66f, FontWeight.Medium),
    val title2SemiBold: TextStyle = style(24, 32, -0.55f, FontWeight.SemiBold),

    val heading1Bold: TextStyle = style(22, 30, -0.26f, FontWeight.Bold),
    val heading1SemiBold: TextStyle = style(22, 30, -0.26f, FontWeight.SemiBold),
    val heading2SemiBold: TextStyle = style(20, 28, -0.24f, FontWeight.SemiBold),
    val headline1SemiBold: TextStyle = style(18, 26, -0.22f, FontWeight.SemiBold),
    val headline2SemiBold: TextStyle = style(17, 24, -0.17f, FontWeight.SemiBold),
    val headline2Medium: TextStyle = style(17, 24, -0.17f, FontWeight.Medium),

    val body1Bold: TextStyle = style(16, 26, -0.16f, FontWeight.Bold),
    val body1SemiBold: TextStyle = style(16, 26, -0.16f, FontWeight.SemiBold),
    val body1Medium: TextStyle = style(16, 24, -0.16f, FontWeight.Medium),
    val body1Regular: TextStyle = style(16, 24, -0.16f, FontWeight.Normal),
    val body2Medium: TextStyle = style(15, 22, -0.15f, FontWeight.Medium),
    val body2Regular: TextStyle = style(15, 24, -0.15f, FontWeight.Normal),

    val label1SemiBold: TextStyle = style(14, 20, -0.14f, FontWeight.SemiBold),
    val label1Medium: TextStyle = style(14, 22, -0.14f, FontWeight.Medium),
    val label2SemiBold: TextStyle = style(13, 18, -0.13f, FontWeight.SemiBold),
    val label2Regular: TextStyle = style(13, 18, -0.13f, FontWeight.Normal),

    val caption1Regular: TextStyle = style(12, 16, -0.12f, FontWeight.Normal),
    val caption2Regular: TextStyle = style(11, 14, -0.11f, FontWeight.Normal),

    val quoteMedium: TextStyle = TextStyle(
        fontFamily = bmjuaFamily,
        lineHeightStyle = defaultLineHeightStyle,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.27f).sp,
        fontWeight = FontWeight.Medium,
    ),
)

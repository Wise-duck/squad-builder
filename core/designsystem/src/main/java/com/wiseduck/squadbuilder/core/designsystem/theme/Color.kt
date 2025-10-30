package com.wiseduck.squadbuilder.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

val MainBg = Color(0xFF0D1116)

val Neutral50 = Color(0xFFFAFAFA)
val Neutral100 = Color(0xFFF5F5F5)
val Neutral200 = Color(0xFFE5E5E5)
val Neutral300 = Color(0xFFD4D4D4)
val Neutral400 = Color(0xFFA1A1A1)
val Neutral500 = Color(0xFF737373)
val Neutral600 = Color(0xFF525252)
val Neutral700 = Color(0xFF404040)
val Neutral800 = Color(0xFF262626)
val Neutral900 = Color(0xFF171717)
val Neutral950 = Color(0xFF0A0A0A)

val Gray900 = Color(0xFF212121)

val Blue500 = Color(0xFF4493F8)

val Blue600 = Color(0xFF2563EB)
val Red500 = Color(0xFFFF443A)
val Green500 = Color(0xFF2ECC71)
val Yellow300 = Color(0xFFFFD743)

val Kakao = Color(0xFFFBD300)

@Immutable
data class SquadBuilderColorScheme(
    val basePrimary: Color = White,
    val baseSecondary: Color = Neutral50,

    val bgPrimary: Color = Neutral900,
    val bgPrimaryPressed: Color = Neutral700,
    val bgSecondary: Color = Neutral100,
    val bgSecondaryPressed: Color = Neutral200,
    val bgTertiary: Color = Neutral50,
    val bgTertiaryPressed: Color = Neutral100,
    val bgDisabled: Color = Neutral200,

    val contentPrimary: Color = Neutral800,
    val contentSecondary: Color = Neutral500,
    val contentTertiary: Color = Neutral400,
    val contentBrand: Color = Blue500,
    val contentDisabled: Color = Neutral400,
    val contentInverse: Color = White,

    val contentError: Color = Red500,
    val contentInfo: Color = Blue500,
    val contentSuccess: Color = Green500,
    val contentWarning: Color = Yellow300,

    val borderPrimary: Color = Neutral200,
    val borderSecondary: Color = Neutral100,
    val borderBrand: Color = Neutral900,
    val borderError: Color = Red500,

    val dividerSm: Color = Neutral200,
    val dividerMd: Color = Neutral100,
)

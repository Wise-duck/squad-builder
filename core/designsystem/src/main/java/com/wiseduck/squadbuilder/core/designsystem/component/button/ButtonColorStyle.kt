package com.wiseduck.squadbuilder.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.theme.Kakao
import com.wiseduck.squadbuilder.core.designsystem.theme.MainComponentBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

enum class ButtonColorStyle {
    STROKE, TEXT, KAKAO, TEXT_WHITE, TEXT_RED;

    @Composable
    fun containerColor(isPressed: Boolean) = when (this) {
        STROKE -> if (isPressed) Neutral800 else MainComponentBg
        TEXT -> Color.Transparent
        KAKAO -> Kakao
        TEXT_WHITE -> if (isPressed) Neutral800 else MainComponentBg
        TEXT_RED -> if (isPressed) Neutral800 else MainComponentBg
    }

    @Composable
    fun contentColor() = when (this) {
        STROKE -> SquadBuilderTheme.colors.contentBrand
        TEXT -> Neutral50
        KAKAO -> SquadBuilderTheme.colors.contentPrimary
        TEXT_WHITE -> Neutral50
        TEXT_RED -> Red500
    }

    @Composable
    fun disabledContainerColor() = when (this) {
        TEXT -> Color.Transparent
        TEXT_WHITE -> Color.Transparent
        TEXT_RED -> Color.Transparent
        else -> SquadBuilderTheme.colors.bgDisabled
    }

    @Composable
    fun disabledContentColor() = SquadBuilderTheme.colors.contentDisabled

    @Composable
    fun borderStroke() = when (this) {
        STROKE -> BorderStroke(1.dp, Neutral500)
        TEXT_WHITE -> BorderStroke(1.dp, Neutral500)
        TEXT_RED -> BorderStroke(1.dp, Neutral500)
        else -> null
    }
}

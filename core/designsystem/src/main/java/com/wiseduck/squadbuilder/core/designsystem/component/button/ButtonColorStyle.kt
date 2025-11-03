package com.wiseduck.squadbuilder.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.theme.Kakao
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral900
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

enum class ButtonColorStyle {
    STROKE, TEXT, KAKAO;

    @Composable
    fun containerColor(isPressed: Boolean) = when (this) {
        STROKE -> if (isPressed) Neutral800 else Neutral900
        TEXT -> Color.Transparent
        KAKAO -> Kakao
    }

    @Composable
    fun contentColor() = when (this) {
        STROKE -> SquadBuilderTheme.colors.contentBrand
        TEXT -> Neutral50
        KAKAO -> SquadBuilderTheme.colors.contentPrimary
    }

    @Composable
    fun disabledContainerColor() = when (this) {
        TEXT -> Color.Transparent
        else -> SquadBuilderTheme.colors.bgDisabled
    }

    @Composable
    fun disabledContentColor() = SquadBuilderTheme.colors.contentDisabled

    @Composable
    fun borderStroke() = when (this) {
        STROKE -> BorderStroke(1.dp, Neutral500)
        else -> null
    }
}

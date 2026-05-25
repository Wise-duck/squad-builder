package com.wiseduck.squadbuilder.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.theme.Kakao

enum class ButtonColorStyle {
    STROKE,
    TEXT,
    KAKAO,
    TEXT_WHITE,
    TEXT_RED,
    ;

    @Composable
    fun containerColor(isPressed: Boolean) =
        when (this) {
            STROKE -> if (isPressed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
            TEXT -> Color.Transparent
            KAKAO -> Kakao
            TEXT_WHITE -> if (isPressed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
            TEXT_RED -> if (isPressed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        }

    @Composable
    fun contentColor() =
        when (this) {
            STROKE -> MaterialTheme.colorScheme.primary
            TEXT -> MaterialTheme.colorScheme.onSurface
            KAKAO -> Color.Black
            TEXT_WHITE -> MaterialTheme.colorScheme.onSurface
            TEXT_RED -> MaterialTheme.colorScheme.error
        }

    @Composable
    fun disabledContainerColor() =
        when (this) {
            TEXT, TEXT_WHITE, TEXT_RED -> Color.Transparent
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(0.38f)
        }

    @Composable
    fun disabledContentColor() = MaterialTheme.colorScheme.primary

    @Composable
    fun borderStroke() =
        when (this) {
            STROKE, TEXT_WHITE, TEXT_RED -> BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline,
            )
            else -> null
        }
}

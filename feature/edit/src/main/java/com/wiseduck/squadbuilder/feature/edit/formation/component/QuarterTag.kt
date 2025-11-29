package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@Composable
fun QuarterTag(
    quarter: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .padding(SquadBuilderTheme.spacing.spacing3)
            .background(
                color = Color.Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(
                    SquadBuilderTheme.radius.md,
                ),
            )
            .padding(SquadBuilderTheme.spacing.spacing2),
        text = "\uD83D\uDD25 Q $quarter",
        color = Red500,
        style = SquadBuilderTheme.typography.body1Regular,
    )
}

@Composable
@ComponentPreview
private fun QuarterTagPreview() {
    SquadBuilderTheme {
        QuarterTag(
            quarter = 1,
        )
    }
}

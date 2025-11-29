package com.wiseduck.squadbuilder.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

private val LightStripeColor = Color(0xFF147D19)
private val DarkStripeColor = Color(0xFF005900)
private val LineColor = Color.White.copy(alpha = 0.5f)

@Composable
fun SoccerField(
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.() -> Unit = {},
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(68f / 105f),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val fieldWidth = size.width
            val fieldHeight = size.height
            val lineWidth = 6.dp.toPx()
            val lineColor = LineColor

            // Stripes
            val stripeHeight = fieldHeight / 10f
            for (i in 0 until 10) {
                drawRect(
                    color = if (i % 2 == 0) LightStripeColor else DarkStripeColor,
                    topLeft = Offset(x = 0f, y = i * stripeHeight),
                    size = Size(width = fieldWidth, height = stripeHeight),
                )
            }

            // Outer Boundary
            drawRect(
                color = lineColor,
                topLeft = Offset.Zero,
                size = size,
                style = Stroke(width = lineWidth),
            )

            // Center Line
            drawLine(
                color = lineColor,
                start = Offset(x = 0f, y = fieldHeight / 2),
                end = Offset(x = fieldWidth, y = fieldHeight / 2),
                strokeWidth = lineWidth,
            )

            // Center Circle
            drawCircle(
                color = lineColor,
                radius = fieldWidth * 0.15f,
                center = center,
                style = Stroke(width = lineWidth),
            )

            // Center Spot
            drawCircle(
                color = lineColor,
                radius = lineWidth * 2,
                center = center,
            )

            // Top Penalty Box
            val penaltyBoxWidth = fieldWidth * 0.6f
            val penaltyBoxHeight = fieldHeight * 0.16f
            drawRect(
                color = lineColor,
                topLeft = Offset(x = (fieldWidth - penaltyBoxWidth) / 2, y = 0f),
                size = Size(width = penaltyBoxWidth, height = penaltyBoxHeight),
                style = Stroke(width = lineWidth),
            )

            // Bottom Penalty Box
            drawRect(
                color = lineColor,
                topLeft = Offset(x = (fieldWidth - penaltyBoxWidth) / 2, y = fieldHeight - penaltyBoxHeight),
                size = Size(width = penaltyBoxWidth, height = penaltyBoxHeight),
                style = Stroke(width = lineWidth),
            )

            // Top Goal Area
            val goalAreaWidth = fieldWidth * 0.3f
            val goalAreaHeight = fieldHeight * 0.055f
            drawRect(
                color = lineColor,
                topLeft = Offset(x = (fieldWidth - goalAreaWidth) / 2, y = 0f),
                size = Size(width = goalAreaWidth, height = goalAreaHeight),
                style = Stroke(width = lineWidth),
            )

            // Bottom Goal Area
            drawRect(
                color = lineColor,
                topLeft = Offset(x = (fieldWidth - goalAreaWidth) / 2, y = fieldHeight - goalAreaHeight),
                size = Size(width = goalAreaWidth, height = goalAreaHeight),
                style = Stroke(width = lineWidth),
            )
        }

        content()
    }
}

@Preview
@Composable
private fun SoccerFieldPreview() {
    SquadBuilderTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SoccerField {
                PlayerChip(
                    modifier = Modifier.align(Alignment.Center),
                    position = "FW",
                    number = "9",
                    name = "Player",
                )
            }
        }
    }
}

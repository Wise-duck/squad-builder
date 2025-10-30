package com.wiseduck.squadbuilder.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

// 옅은 초록 스트라이프 색상
private val LightStripeColor = Color(0xFF147D19)
// 진한 초록 스트라이프 색상
private val DarkStripeColor = Color(0xFF005900)
// 축구장 라인 색상
private val LineColor = Color.White.copy(alpha = 0.5f)

@Composable
fun SoccerField(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            // 일반적인 축구장 비율 (가로:68m, 세로:105m)에 맞춰 화면 비율을 설정합니다.
            .aspectRatio(68f / 105f)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val fieldWidth = size.width
            val fieldHeight = size.height
            val lineWidth = 3.dp.toPx()

            // 1. 스트라이프 배경 그리기
            val stripeHeight = fieldHeight / 10f
            for (i in 0 until 10) {
                drawRect(
                    color = if (i % 2 == 0) LightStripeColor else DarkStripeColor,
                    topLeft = Offset(x = 0f, y = i * stripeHeight),
                    size = Size(width = fieldWidth, height = stripeHeight)
                )
            }

            // 2. 축구장 라인 그리기
            // 외곽선
            drawRect(
                color = LineColor,
                style = Stroke(width = lineWidth)
            )

            // 중앙선 (가로로 수정)
            drawLine(
                color = LineColor,
                start = Offset(x = 0f, y = fieldHeight / 2),
                end = Offset(x = fieldWidth, y = fieldHeight / 2),
                strokeWidth = lineWidth
            )

            // 중앙 원
            drawCircle(
                color = LineColor,
                radius = fieldWidth * 0.15f,
                style = Stroke(width = lineWidth)
            )

            // 위쪽 페널티 에어리어
            drawRect(
                color = LineColor,
                topLeft = Offset(x = fieldWidth * 0.2f, y = 0f),
                size = Size(width = fieldWidth * 0.6f, height = fieldHeight * 0.16f),
                style = Stroke(width = lineWidth)
            )

            // 아래쪽 페널티 에어리어
            drawRect(
                color = LineColor,
                topLeft = Offset(x = fieldWidth * 0.2f, y = fieldHeight * (1 - 0.16f)),
                size = Size(width = fieldWidth * 0.6f, height = fieldHeight * 0.16f),
                style = Stroke(width = lineWidth)
            )

            val goalWidth = fieldWidth * 0.3f
            val goalDepth = fieldHeight * 0.03f // 골대의 깊이

            // 위쪽 골대
            drawRect(
                color = LineColor,
                topLeft = Offset(x = (fieldWidth - goalWidth) / 2, y = 0f),
                size = Size(width = goalWidth, height = goalDepth),
                style = Stroke(width = lineWidth)
            )

            // 아래쪽 골대
            drawRect(
                color = LineColor,
                topLeft = Offset(x = (fieldWidth - goalWidth) / 2, y = fieldHeight - goalDepth),
                size = Size(width = goalWidth, height = goalDepth),
                style = Stroke(width = lineWidth)
            )
        }
    }
}

@Preview
@Composable
private fun SoccerFieldPreview() {
    SquadBuilderTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SoccerField()
        }
    }
}
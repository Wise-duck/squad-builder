package com.wiseduck.squadbuilder.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue600
import com.wiseduck.squadbuilder.core.designsystem.theme.Gray900
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@Composable
fun PlayerChip(
    modifier: Modifier = Modifier,
    position: String,
    number: String,
    name: String,
    shirtColor: Color = Blue600, // 색상을 파라미터로 받아 유연하게
    textColor: Color = Neutral100
) {
    // 1. 전체 레이아웃을 Column으로 잡습니다.
    Column(
        modifier = modifier.size(width = 64.dp, height = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 2. 원형 셔츠와 포지션 텍스트를 담는 Box (React 코드의 상대 위치 지정과 유사)
        Box(
            modifier = Modifier.size(56.dp), // 셔츠(48dp) + 포지션 텍스트 높이
            contentAlignment = Alignment.Center
        ) {
            // 원형 셔츠
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(shirtColor)
                    .border(
                        BorderStroke(2.dp, Neutral100),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 중앙 등번호
                Text(
                    text = number,
                    color = textColor,
                    fontSize = 22.sp, // text-2xl
                    fontWeight = FontWeight.Black, // font-black
                    textAlign = TextAlign.Center
                )
            }

            // 상단 포지션
            Text(
                text = position,
                modifier = Modifier
                    .align(Alignment.TopCenter) // Box 상단 중앙에 배치
                    .offset(y = (-4).dp) // 살짝 위로 이동 (React의 -top-3)
                    .background(Gray900.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                color = textColor,
                fontSize = 10.sp, // text-xs
                fontWeight = FontWeight.SemiBold
            )
        }

        // 3. 하단 이름
        Text(
            text = name,
            modifier = Modifier.padding(top = 4.dp), // mt-1
            color = Neutral100,
            fontSize = 11.sp, // text-xs
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun PlayerChipPreview() {
    SquadBuilderTheme {
        Box(
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(16.dp)
        ) {
            PlayerChip(
                position = "FW",
                number = "7",
                name = "Son"
            )
        }
    }
}
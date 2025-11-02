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
    shirtColor: Color = Blue600,
    textColor: Color = Neutral100
) {
    Column(
        modifier = modifier.size(width = 56.dp, height = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(shirtColor)
                    .border(
                        BorderStroke(1.5.dp, Neutral100),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = position,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-3).dp)
                    .background(Gray900.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 1.dp),
                color = textColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = name,
            modifier = Modifier,
            color = Neutral100,
            fontSize = 10.sp,
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
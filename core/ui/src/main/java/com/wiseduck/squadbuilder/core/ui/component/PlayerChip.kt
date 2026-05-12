package com.wiseduck.squadbuilder.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@Composable
fun PlayerChip(
    modifier: Modifier = Modifier,
    position: String,
    number: String,
    name: String,
    shirtColor: Color = MaterialTheme.colorScheme.secondary,
    textColor: Color = Color.White,
) {
    Column(
        modifier = modifier
            .size(width = 64.dp, height = 80.dp)
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(shirtColor)
                    .border(1.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                )
            }

            Text(
                text = position,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-8).dp)
                    .background(Color.Black, RoundedCornerShape(2.dp))
                    .padding(horizontal = 4.dp, vertical = 1.dp),
                color = Color.White,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@ComponentPreview
@Composable
private fun PlayerChipPreview() {
    SquadBuilderTheme {
        PlayerChip(
            position = "FW",
            number = "7",
            name = "Son",
        )
    }
}

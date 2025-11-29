package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.PlayerPosition
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.edit.R

@Composable
fun PlayerListCard(
    modifier: Modifier = Modifier,
    name: String,
    backNumber: Int,
    position: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = position,
                style = SquadBuilderTheme.typography.body1Bold,
                color = PlayerPosition.getColor(position),
                fontWeight = FontWeight.Bold,
            )

            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_shirt),
                    contentDescription = "Shirt Icon",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White,
                )
                Text(
                    text = backNumber.toString(),
                    style = SquadBuilderTheme.typography.body1Bold,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }

            Text(
                text = name,
                style = SquadBuilderTheme.typography.body2Medium,
                color = Color.White,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerListCardPreview() {
    SquadBuilderTheme {
        PlayerListCard(
            name = "손흥민",
            backNumber = 7,
            position = "FW",
            onClick = {},
        )
//        PlayerListCard(
//            name = "이강인",
//            backNumber = 18,
//            position = "MF",
//            onClick = {}
//        )
//        PlayerListCard(
//            name = "김민재",
//            backNumber = 3,
//            position = "DF",
//            onClick = {}
//        )
//        PlayerListCard(
//            name = "조현우",
//            backNumber = 21,
//            position = "GK",
//            onClick = {}
//        )
    }
}

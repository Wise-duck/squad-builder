package com.wiseduck.squadbuilder.feature.detail.team.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainComponentBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.detail.R

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MainComponentBg,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral500,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = title,
                modifier = Modifier.size(SquadBuilderTheme.spacing.spacing10),
                tint = Neutral100,
            )

            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            Text(
                text = title,
                style = SquadBuilderTheme.typography.title1Bold,
                color = Neutral100,
            )

            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            Text(
                text = description,
                style = SquadBuilderTheme.typography.body1Bold,
                color = Green500,
            )
        }
    }
}

@Preview
@Composable
private fun MenuButtonPreview() {
    SquadBuilderTheme {
        MenuButton(
            iconRes = R.drawable.ic_player,
            title = "선수 관리",
            description = "팀의 선수 목록을 확인하고 편집합니다.",
            onClick = {},
        )
    }
}

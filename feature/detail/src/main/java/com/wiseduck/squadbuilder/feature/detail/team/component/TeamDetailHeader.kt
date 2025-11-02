package com.wiseduck.squadbuilder.feature.detail.team.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.White
import com.wiseduck.squadbuilder.core.ui.R

@Composable
fun TeamDetailHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MainBg)
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = modifier,
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back Icon",
                    tint = White
                )
            }
            Spacer(
                modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4)
            )
            Text(
                "팀 상세 화면",
                style = SquadBuilderTheme.typography.title1Bold,
                color = White
            )
        }
        Spacer(
            modifier = modifier.fillMaxWidth()
                .height(SquadBuilderTheme.spacing.spacing05)
                .background(Neutral800)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamDetailPreview() {
    SquadBuilderTheme {
        TeamDetailHeader(
            onBackClick = {}
        )
    }
}

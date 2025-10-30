package com.wiseduck.squadbuilder.feature.settings.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.designsystem.theme.Yellow300

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    name: String = "익명"
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            MainBg
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral500
        )
    ) {
        Column(
            Modifier.padding(SquadBuilderTheme.spacing.spacing4)
        ) {
            Text(
                text = "계정 정보",
                color = Neutral100,
                style = SquadBuilderTheme.typography.heading1Bold
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )
            Row {
                Text(
                    "이름",
                    color = Neutral500
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                name?.let {
                    Text(
                        text = name,
                        color = Green500
                    )
                }

            }
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2)
            )
            Row {
                Text(
                    text = "로그인 방식",
                    color = Neutral500
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "카카오 로그인",
                    color = Yellow300
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ProfileCardPreview() {
    ProfileCard(
        name = "주름이"
    )
}
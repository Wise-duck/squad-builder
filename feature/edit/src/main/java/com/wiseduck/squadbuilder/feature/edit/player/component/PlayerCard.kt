package com.wiseduck.squadbuilder.feature.edit.player.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.PlayerPosition
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.feature.edit.R

@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: TeamPlayerModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MainBg
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral500
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(SquadBuilderTheme.spacing.spacing4)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = SquadBuilderTheme.spacing.spacing4)
                            .weight(0.4f),
                        text = player.position,
                        style = SquadBuilderTheme.typography.body1Bold,
                        color = PlayerPosition.getColor(player.position)
                    )
                    Text(
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(start = SquadBuilderTheme.spacing.spacing4),
                        text = player.name,
                        style = SquadBuilderTheme.typography.body1SemiBold,
                        color = Neutral300
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = SquadBuilderTheme.spacing.spacing4),
                    text = "등번호: ${player.backNumber}",
                    style = SquadBuilderTheme.typography.body1Regular,
                    color = Neutral300
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onEditClick,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        tint = Blue500,
                        contentDescription = "Edit Icon",
                    )
                }
                Spacer(modifier = Modifier.width(SquadBuilderTheme.spacing.spacing1))
                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        painter = painterResource(com.wiseduck.squadbuilder.core.designsystem.R.drawable.ic_remove),
                        tint = Red500,
                        contentDescription = "Delete Icon"
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerCardPreview() {
    SquadBuilderTheme {
        PlayerCard(
            onEditClick = {},
            onDeleteClick = {},
            player = TeamPlayerModel(
                id = 1,
                teamId = 1,
                name = "주ㄹ므",
                backNumber = 1,
                position = "MF"
            )
        )
    }
}
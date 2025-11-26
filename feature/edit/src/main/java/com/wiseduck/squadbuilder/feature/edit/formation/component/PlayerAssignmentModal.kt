package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.smallButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel
import com.wiseduck.squadbuilder.feature.edit.R

@Composable
fun PlayerAssignmentModal(
    availablePlayers: List<TeamPlayerModel>,
    onDismissRequest: () -> Unit,
    onAssignPlayer: (Int) -> Unit
) {
    val positionOrder = listOf("FW", "MF", "DF", "GK")

    val sortedPlayers = availablePlayers.sortedBy { player ->
        val index = positionOrder.indexOf(player.position.uppercase())
        if (index == -1) Int.MAX_VALUE else index
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MainBg,
                    shape = RoundedCornerShape(SquadBuilderTheme.radius.md)
                )
                .border(
                    width = 1.dp,
                    color = Neutral500,
                    shape = RoundedCornerShape(size = SquadBuilderTheme.radius.md)
                )
                .padding(SquadBuilderTheme.spacing.spacing4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.player_assignment_modal_title),
                color = Neutral100,
                style = SquadBuilderTheme.typography.title1Bold
            )
            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedPlayers) { player ->
                    PlayerListCard(
                        name = player.name,
                        backNumber = player.backNumber,
                        position = player.position,
                        onClick = { onAssignPlayer(player.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            SquadBuilderButton(
                onClick = { onDismissRequest() },
                text = stringResource(R.string.formation_close_button),
                sizeStyle = smallButtonStyle.copy(
                    paddingValues = PaddingValues(
                        horizontal = SquadBuilderTheme.spacing.spacing3,
                        vertical = 5.dp
                    )
                ),
                colorStyle = ButtonColorStyle.TEXT_WHITE,
                modifier = Modifier
                    .width(60.dp)
                    .defaultMinSize(minHeight = 1.dp)
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerAssignmentModalPreview() {
    SquadBuilderTheme {
        PlayerAssignmentModal(
            availablePlayers = listOf(
                TeamPlayerModel(teamId = 1, id = 1, name = "손흥민", backNumber = 7, position = "FW"),
                TeamPlayerModel(teamId = 1, id = 2, name = "김민재", backNumber = 3, position = "DF"),
                TeamPlayerModel(teamId = 1, id = 3, name = "이강인", backNumber = 18, position = "MF"),
                TeamPlayerModel(teamId = 1, id = 4, name = "조현우", backNumber = 1, position = "GK"),
            ),
            onDismissRequest = {},
            onAssignPlayer = {}
        )
    }
}
package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.PlayerPosition
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainComponentBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral400
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.PlayerQuarterStatus
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.feature.edit.formation.mock.dummyPlayerQuarterStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PlayerQuarterStatusSideBar(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    playerQuarterStatus: ImmutableList<PlayerQuarterStatus>,
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { onDismissRequest() }
            }
            .fillMaxSize()
            .drawWithContent {
                drawContent()
            },
        contentAlignment = Alignment.CenterEnd,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        bottomStart = 10.dp,
                    ),
                )
                .background(MainComponentBg)
                .padding(SquadBuilderTheme.spacing.spacing4)
                .pointerInput(Unit) {
                    detectTapGestures { }
                },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_player_check),
                    contentDescription = "Player Check Icon",
                    tint = Green500,
                )
                Spacer(
                    modifier = Modifier
                        .width(SquadBuilderTheme.spacing.spacing2),
                )
                Text(
                    text = stringResource(R.string.player_side_bar_title),
                    style = SquadBuilderTheme.typography.heading1Bold,
                    color = Neutral50,
                )
            }
            Spacer(
                modifier = Modifier
                    .height(SquadBuilderTheme.spacing.spacing2),
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Neutral800),
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SquadBuilderTheme.spacing.spacing2),
            )

            if (playerQuarterStatus.isEmpty()) {
                Text(
                    text = stringResource(R.string.player_side_bar_no_content_description),
                    style = SquadBuilderTheme.typography.body1Regular,
                    color = Neutral400,
                )
            }

            LazyColumn {
                items(playerQuarterStatus.size) { index ->
                    PlayerQuarterStatusRow(
                        modifier = modifier,
                        playerQuarterStatus = playerQuarterStatus,
                        index = index,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerQuarterStatusRow(
    modifier: Modifier = Modifier,
    playerQuarterStatus: ImmutableList<PlayerQuarterStatus>,
    index: Int,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SquadBuilderTheme.spacing.spacing2),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${playerQuarterStatus[index].backNumber}.",
                style = SquadBuilderTheme.typography.body1Bold,
                color = Neutral500,
            )
            Spacer(
                modifier = Modifier
                    .width(SquadBuilderTheme.spacing.spacing2),
            )

            Text(
                text = playerQuarterStatus[index].position,
                style = SquadBuilderTheme.typography.body1Bold,
                color = PlayerPosition.getColor(playerQuarterStatus[index].position),
            )
            Spacer(
                modifier = Modifier
                    .width(SquadBuilderTheme.spacing.spacing2),
            )

            Text(
                text = playerQuarterStatus[index].playerName,
                style = SquadBuilderTheme.typography.body1Bold,
                color = Neutral50,
            )
            Spacer(
                modifier = Modifier
                    .width(SquadBuilderTheme.spacing.spacing1),
            )

            Text(
                text = stringResource(
                    R.string.assigned_quarter_size,
                    playerQuarterStatus[index].quarters.size,
                ),
                style = SquadBuilderTheme.typography.caption1Regular,
                color = Neutral300,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(
                    id = R.string.assigned_quarters_label,
                    playerQuarterStatus[index].quarters.joinToString(", "),
                ),
                style = SquadBuilderTheme.typography.caption1Regular,
                color = Neutral300,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerQuarterStatusSideBarPreview() {
    SquadBuilderTheme {
        PlayerQuarterStatusSideBar(
            onDismissRequest = {},
            playerQuarterStatus = dummyPlayerQuarterStatus.toPersistentList(),
        )
    }
}

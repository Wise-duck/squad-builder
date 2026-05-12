package com.wiseduck.squadbuilder.feature.edit.player.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamPlayer
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.feature.edit.player.PlayerUiEvent
import com.wiseduck.squadbuilder.feature.edit.player.PlayerUiState
import com.wiseduck.squadbuilder.feature.edit.player.mock.fakePlayerUiStateMock
import com.wiseduck.squadbuilder.feature.edit.player.mock.fakePlayers
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PlayerList(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
    players: ImmutableList<TeamPlayer>,
    currentEditingPlayerId: Int?,
    onPlayerDeleteClick: (Int) -> Unit,
    onPlayerEditClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = players,
            key = { player -> player.id },
        ) { player ->
            val isCurrentlyEditing = currentEditingPlayerId == player.id

            PlayerCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                player = player,
                onDeleteClick = { onPlayerDeleteClick(player.id) },
                onEditClick = { onPlayerEditClick(player.id) },
            )
            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2))

            if (isCurrentlyEditing) {
                PlayerFormCard(
                    title = stringResource(
                        R.string.player_form_card_edit_title,
                        player.name,
                    ),
                    player = player,
                    commitButtonText = stringResource(R.string.player_form_card_save_button),
                    onCommitButtonClick = { playerId, name, position, backNumber ->
                        state.eventSink(
                            PlayerUiEvent.OnPlayerUpdateConfirm(
                                playerId = playerId!!,
                                name = name,
                                position = position,
                                backNumber = backNumber,
                            ),
                        )
                    },
                    onCancelButtonClick = {
                        state.eventSink(PlayerUiEvent.OnPlayerUpdateCancel)
                    },
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerListPreview() {
    SquadBuilderTheme {
        PlayerList(
            state = fakePlayerUiStateMock,
            players = fakePlayers.toPersistentList(),
            currentEditingPlayerId = 1,
            onPlayerDeleteClick = {},
            onPlayerEditClick = {},
        )
    }
}

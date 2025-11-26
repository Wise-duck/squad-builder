package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.wiseduck.squadbuilder.feature.edit.R

@Composable
fun PlayerInfoModal(
    playerName: String,
    playerPosition: String,
    playerBackNumber: String,
    onModifyClick: () -> Unit,
    onUnassignClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(onDismissRequest = onUnassignClick) {
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
                text = stringResource(R.string.player_info_modal_title),
                color = Neutral100,
                style = SquadBuilderTheme.typography.title1Bold
            )
            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            PlayerListCard(
                name = playerName,
                position = playerPosition,
                backNumber = playerBackNumber.toIntOrNull() ?: 0,
                onClick = {} // 카드 자체는 클릭되지 않도록 설정
            )

            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SquadBuilderTheme.spacing.spacing2)
            ) {
                SquadBuilderButton(
                    onClick = onUnassignClick,
                    text = stringResource(R.string.player_info_modal_delete_button),
                    sizeStyle = smallButtonStyle,
                    colorStyle = ButtonColorStyle.TEXT_RED,
                    modifier = Modifier.weight(1f)
                )
                SquadBuilderButton(
                    onClick = onModifyClick,
                    text = stringResource(R.string.player_info_modal_edit_button),
                    sizeStyle = smallButtonStyle,
                    colorStyle = ButtonColorStyle.STROKE,
                    modifier = Modifier.weight(1f)
                )
                SquadBuilderButton(
                    onClick = onCancelClick,
                    text = stringResource(R.string.formation_close_button),
                    sizeStyle = smallButtonStyle,
                    colorStyle = ButtonColorStyle.TEXT_WHITE,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerInfoModalPreview() {
    SquadBuilderTheme {
        PlayerInfoModal(
            playerName = "손흥민",
            playerPosition = "FW",
            playerBackNumber = "7",
            onModifyClick = {},
            onUnassignClick = {},
            onCancelClick = {}
        )
    }
}
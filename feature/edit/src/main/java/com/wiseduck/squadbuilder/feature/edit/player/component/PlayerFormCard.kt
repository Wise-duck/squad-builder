package com.wiseduck.squadbuilder.feature.edit.player.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.smallButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Blue500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.TeamPlayerModel

typealias OnFormCommitClick = (id: Int?, name: String, position: String, backNumber: Int) -> Unit

@Composable
fun PlayerFormCard(
    modifier: Modifier = Modifier,
    title: String,
    player: TeamPlayerModel? = null,
    commitButtonText: String,
    onCommitButtonClick: OnFormCommitClick,
    onCancelButtonClick: () -> Unit
) {
    val initialName = player?.name ?: ""
    val initialBackNumber = player?.backNumber ?: 0
    val initialPosition = player?.position ?: ""
    val playerId = player?.id

    var name by remember(player?.id) { mutableStateOf(initialName) }
    var backNumber by remember(player?.id) { mutableIntStateOf(initialBackNumber) }
    var position by remember(player?.id) { mutableStateOf(initialPosition) }
    var backNumberString by remember(player?.id) { mutableStateOf(if (initialBackNumber == 0) "" else initialBackNumber.toString()) }

    val isBackNumberValid = backNumber > 0
    val isFormValid = name.isNotBlank() && position.isNotBlank() && isBackNumberValid

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            MainBg
        ),
        border = BorderStroke(
            1.dp,
            Neutral500
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4),
        ) {
            Text(
                text = title,
                color = Neutral100,
                style = SquadBuilderTheme.typography.heading1SemiBold
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("이름", color = Neutral500) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Neutral100,
                    unfocusedTextColor = Neutral100,
                    cursorColor = Blue500,
                    unfocusedContainerColor = MainBg,
                    focusedContainerColor = MainBg
                )
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )

            OutlinedTextField(
                value = backNumberString,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    backNumberString = filteredValue
                    backNumber = filteredValue.toIntOrNull() ?: 0
                },
                label = { Text("등번호", color = Neutral500) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = !isBackNumberValid && backNumberString.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Neutral100,
                    unfocusedTextColor = Neutral100,
                    cursorColor = Blue500,
                    unfocusedContainerColor = MainBg,
                    focusedContainerColor = MainBg
                )
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )

//            Text(
//                text = "포지션 선택(FW, MF, DF, GK)",
//                color = Neutral500,
//                style = SquadBuilderTheme.typography.body1Regular
//            )
            OutlinedTextField(
                value = position,
                onValueChange = { position = it },
                label = { Text("포지션 선택(FW, MF, DF, GK)", color = Neutral500) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Neutral100,
                    unfocusedTextColor = Neutral100,
                    cursorColor = Blue500,
                    unfocusedContainerColor = MainBg,
                    focusedContainerColor = MainBg
                )
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2)
            )

            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SquadBuilderButton(
                    text = commitButtonText,
                    onClick = { onCommitButtonClick(playerId, name, position, backNumber) },
                    sizeStyle = smallButtonStyle,
                    colorStyle = ButtonColorStyle.STROKE,
                    enabled = isFormValid
                )
                Spacer(
                    modifier = Modifier.width(SquadBuilderTheme.spacing.spacing4)
                )
                SquadBuilderButton(
                    text = "취소",
                    onClick = onCancelButtonClick,
                    sizeStyle = smallButtonStyle,
                    colorStyle = ButtonColorStyle.STROKE
                )
            }

        }
    }
}

@ComponentPreview
@Composable
private fun PlayerFormCardPreview() {
    SquadBuilderTheme {
        PlayerFormCard(
            title = "선수1 정보 수정",
            player = TeamPlayerModel(id = 1, teamId = 1, name = "선수1", backNumber = 1, position = "MD"),
            commitButtonText = "수정 완료",
            onCommitButtonClick = { _, _, _, _ -> },
            onCancelButtonClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        PlayerFormCard(
            title = "새 선수 생성",
            player = null,
            commitButtonText = "생성",
            onCommitButtonClick = { _, _, _, _ -> },
            onCancelButtonClick = {}
        )
//        PlayerFormCard(
//            title = "선수 정보 수정",
//            player = TeamPlayerModel(
//                id = 1,
//                teamId = 1,
//                name = "선수 이름",
//                backNumber = 1,
//                position = "MD"
//            ),
//            commitButtonText = "선수 정보 수정",
//            onCommitButtonClick = {
//                id, name, position, backNumber ->
//            },
//            onCancelButtonClick = {}
//        )
    }
}
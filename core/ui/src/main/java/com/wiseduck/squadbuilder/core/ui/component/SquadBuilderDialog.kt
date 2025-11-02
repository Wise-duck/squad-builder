package com.wiseduck.squadbuilder.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.smallButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral300
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@Composable
fun SquadBuilderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmRequest: () -> Unit,
    dismissButtonText: String? = null,
    confirmButtonText: String,
    title: String? = null,
    description: String? = null,
    properties: DialogProperties = DialogProperties(),
) {
    SquadBuilderDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest,
        dismissButtonText = dismissButtonText,
        confirmButtonText = confirmButtonText,
        title = title,
        content = {
            description?.let {
                Text(
                    text = it,
                    color = Neutral300
                )
            }
        },
        properties = properties
    )
}

@Composable
fun SquadBuilderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmRequest: () -> Unit,
    dismissButtonText: String? = null,
    confirmButtonText: String,
    title: String? = null,
    content: @Composable (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = properties,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(SquadBuilderTheme.spacing.spacing4)
                .background(
                    MainBg,
                    shape = RoundedCornerShape(SquadBuilderTheme.radius.md)
                )
                .border(
                    width = 1.dp,
                    color = Neutral500,
                    shape = RoundedCornerShape(size = SquadBuilderTheme.radius.md)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            title?.let {
                Text(
                    text = title,
                    color = Neutral100
                )
            }
            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            content?.invoke()

            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            Row(modifier = Modifier.fillMaxWidth()) {
                dismissButtonText?.let {
                    SquadBuilderButton(
                        onClick = { onDismissRequest() },
                        text = dismissButtonText,
                        sizeStyle = smallButtonStyle,
                        colorStyle = ButtonColorStyle.TEXT,
                        modifier = Modifier.weight(1f),
                    )
                }
                SquadBuilderButton(
                    onClick = { onConfirmRequest() },
                    text = confirmButtonText,
                    sizeStyle = smallButtonStyle,
                    colorStyle = ButtonColorStyle.TEXT,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}


@ComponentPreview
@Composable
private fun SquadBuilderDialogPreview() {
    SquadBuilderDialog(
        onConfirmRequest = {},
        confirmButtonText = "확인",
        dismissButtonText = "취소",
        title = "다이얼로그 제목",
        content = {
            Text(
                text = "Composable을 받는 다이얼로그 내용입니다.",
                color = Neutral300
            )
        }
    )
}

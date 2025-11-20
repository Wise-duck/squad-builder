package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.wiseduck.squadbuilder.feature.edit.R
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral50
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog

@Composable
fun QuarterSelectionDialog(
    modifier: Modifier = Modifier,
    onConfirm: (Set<Int>) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedQuarters by remember { mutableStateOf(emptySet<Int>()) }
    val quarters = (1..4).toList()

    SquadBuilderDialog(
        onConfirmRequest = { onConfirm(selectedQuarters)},
        onDismissRequest = onDismiss,
        confirmButtonText = stringResource(R.string.multiple_share_dialog_confirm_button),
        dismissButtonText = stringResource(R.string.multiple_share_dialog_cancle_button),
        title = stringResource(R.string.multiple_share_dialog_title),
        content = {
            Column {
                quarters.forEach { quarter ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .toggleable(
                                value = quarter in selectedQuarters,
                                role = Role.Checkbox,
                                onValueChange = { checked ->
                                    selectedQuarters = if (checked) {
                                        selectedQuarters + quarter
                                    } else {
                                        selectedQuarters - quarter
                                    }
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Q $quarter",
                            color = Neutral50,
                            style = SquadBuilderTheme.typography.body1Regular
                        )
                        Checkbox(
                            checked = quarter in selectedQuarters,
                            onCheckedChange = null
                        )
                    }
                }
            }
        }
    )
}

@ComponentPreview
@Composable
private fun QuarterSelectionDialogPreview() {
    SquadBuilderTheme {
        QuarterSelectionDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
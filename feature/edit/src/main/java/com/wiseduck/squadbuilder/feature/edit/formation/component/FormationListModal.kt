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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.smallButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral100
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.FormationListItemModel

@Composable
fun FormationListModal(
    formationList: List<FormationListItemModel>,
    onDismissRequest: () -> Unit,
    onFormationCardClick: (Int) -> Unit,
    onDeleteFormationClick: (Int) -> Unit
) {
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
                text = stringResource(
                    R.string.formation_list_modal_title
                ),
                color = Green500,
                style = SquadBuilderTheme.typography.title1Medium
            )
            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(formationList) { formationItem ->
                    FormationCard(
                        formation = formationItem,
                        onClick = { onFormationCardClick(formationItem.formationId) },
                        onDeleteClick = { onDeleteFormationClick(formationItem.formationId) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(SquadBuilderTheme.spacing.spacing4))

            SquadBuilderButton(
                onClick = { onDismissRequest() },
                text = stringResource(
                    R.string.formation_list_modal_close_button
                ),
                sizeStyle = smallButtonStyle.copy(
                    paddingValues = PaddingValues(
                        horizontal = SquadBuilderTheme.spacing.spacing3, // 기존 좌우 여백
                        vertical = 5.dp
                    )
                ),
                colorStyle = ButtonColorStyle.TEXT_WHITE,
                modifier = Modifier.width(60.dp)
                    .defaultMinSize(minHeight = 1.dp)
            )
        }
    }
}

@ComponentPreview
@Composable
private fun FormationListModalPreview() {
    SquadBuilderTheme {
        FormationListModal(
            formationList = listOf(
                FormationListItemModel(1, "4-3-3", "2023-01-01T00:00:00Z"),
                FormationListItemModel(2, "4-4-2", "2023-01-02T00:00:00Z"),
                FormationListItemModel(3, "3-5-2", "2023-01-03T00:00:00Z")
            ),
            onDismissRequest = {},
            onFormationCardClick = {},
            onDeleteFormationClick = {}
        )
    }
}

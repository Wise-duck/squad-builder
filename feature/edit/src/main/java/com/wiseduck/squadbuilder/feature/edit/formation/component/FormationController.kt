package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Red500
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.feature.edit.R
import com.wiseduck.squadbuilder.core.designsystem.theme.White

@Composable
fun FormationController(
    modifier: Modifier = Modifier,
    teamName: String,
    onFormationResetClick: () -> Unit,
    onFormationListClick: () -> Unit,
    onFormationSaveClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MainBg)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_formation_team),
                contentDescription = "팀 아이콘",
                tint = White
            )

            Text(
                text = teamName,
                style = SquadBuilderTheme.typography.title1Bold,
                modifier = Modifier.padding(start = 12.dp),
                color = White
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onFormationResetClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_reset),
                    contentDescription = "초기화",
                    tint = Red500
                )
            }

            IconButton(onClick = onFormationListClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_list),
                    contentDescription = "목록",
                    tint = White
                )
            }

            IconButton(onClick = onFormationSaveClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = "저장",
                    tint = Green500
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun FormationControllerPreview() {
    SquadBuilderTheme {
        FormationController(
            teamName = "비안코",
            onFormationResetClick = {},
            onFormationListClick = {},
            onFormationSaveClick = {}
        )
    }
}
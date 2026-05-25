package com.wiseduck.squadbuilder.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.component.button.ButtonColorStyle
import com.wiseduck.squadbuilder.core.designsystem.component.button.SquadBuilderButton
import com.wiseduck.squadbuilder.core.designsystem.component.button.mediumButtonStyle
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.component.AdBanner
import com.wiseduck.squadbuilder.feature.home.R

@Composable
fun GuestModeHomeUiContent(
    modifier: Modifier = Modifier,
    adUnitId: String,
    onLoginClick: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = "Lock Icon",
                tint = Color.Black,
                modifier = Modifier.size(300.dp),
            )
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.guest_mode_team_list_empty),
                    style = SquadBuilderTheme.typography.body1Regular,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(SquadBuilderTheme.spacing.spacing6),
                )
                SquadBuilderButton(
                    text = stringResource(R.string.login_text_button),
                    onClick = onLoginClick,
                    sizeStyle = mediumButtonStyle,
                    colorStyle = ButtonColorStyle.TEXT,
                )
            }
        }

        AdBanner(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = SquadBuilderTheme.spacing.spacing4,
                    horizontal = SquadBuilderTheme.spacing.spacing4,
                ),
            adUnitId = adUnitId,
        )
    }
}

@ComponentPreview
@Composable
private fun GuestModeHomeUiContentPreview() {
    SquadBuilderTheme {
        GuestModeHomeUiContent(
            adUnitId = "",
            onLoginClick = {},
        )
    }
}

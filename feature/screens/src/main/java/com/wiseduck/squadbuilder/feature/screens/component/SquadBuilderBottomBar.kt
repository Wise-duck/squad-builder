package com.wiseduck.squadbuilder.feature.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.Green500
import com.wiseduck.squadbuilder.core.designsystem.theme.MainBg
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral500
import com.wiseduck.squadbuilder.core.designsystem.theme.Neutral800
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold

@Composable
fun SquadBuilderBottomBar(
    modifier: Modifier = Modifier,
    currentTab: SquadBuilderBottomTab,
    onTabSelected: (SquadBuilderBottomTab) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MainBg),
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Neutral800),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SquadBuilderBottomTab.entries.forEach { tab ->
                    BottomBarItem(
                        tab = tab,
                        isSelected = (tab == currentTab),
                        onClick = {
                            onTabSelected(tab)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    tab: SquadBuilderBottomTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val iconRes = if (isSelected) tab.selectedIconResId else tab.iconResId
            val textColor = if (isSelected) Green500 else Neutral500

            Icon(
                painter = painterResource(iconRes),
                contentDescription = "Bottom Tab Icon"
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2)
            )
            Text(
                text = stringResource(tab.labelResId),
                color = textColor,
                style = SquadBuilderTheme.typography.caption1Regular
            )
        }
    }
}

@DevicePreview
@Composable
private fun SquadBuilderBottomBarPreview() {
    SquadBuilderTheme {
        SquadBuilderScaffold(
            bottomBar = {
                SquadBuilderBottomBar(
                    currentTab = SquadBuilderBottomTab.PROFILE,
                    onTabSelected = {}
                )
            }
        ) {

        }
    }
}
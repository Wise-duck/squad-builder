package com.wiseduck.squadbuilder.feature.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.DevicePreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SquadBuilderBottomBar(
    modifier: Modifier = Modifier,
    tabs: ImmutableList<SquadBuilderBottomTab>,
    currentTab: SquadBuilderBottomTab,
    onTabSelected: (SquadBuilderBottomTab) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outline),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEach { tab ->
                    BottomBarItem(
                        tab = tab,
                        isSelected = (tab == currentTab),
                        onClick = {
                            onTabSelected(tab)
                        },
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
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

            Icon(
                painter = painterResource(tab.iconResId),
                contentDescription = "Bottom Tab Icon",
                tint = color,
            )
            Spacer(
                modifier = Modifier.height(SquadBuilderTheme.spacing.spacing2),
            )
            Text(
                text = stringResource(tab.labelResId),
                color = color,
                style = SquadBuilderTheme.typography.caption1Regular,
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
                    tabs = SquadBuilderBottomTab.entries.toImmutableList(),
                    currentTab = SquadBuilderBottomTab.PROFILE,
                    onTabSelected = {},
                )
            },
        ) {
        }
    }
}

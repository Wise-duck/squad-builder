package com.wiseduck.squadbuilder.feature.edit.player.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@Composable
fun PlayerCreateSection(
    modifier: Modifier = Modifier,
    onPlayerCreateButtonClick: () -> Unit,
) {

}

@ComponentPreview
@Composable
fun PlayerCreateSection() {
    SquadBuilderTheme {
        PlayerCreateSection()
    }
}
package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.model.Placement
import com.wiseduck.squadbuilder.core.ui.component.SoccerField
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FormationBoard(
    modifier: Modifier = Modifier,
    players: ImmutableList<Placement>,
    onPlayerClick: (Int) -> Unit,
    onPlayerDragStart: (Int) -> Unit,
    onPlayerDrag: (Int, Float, Float) -> Unit,
    onPlayerDragEnd: (Int, Float, Float) -> Unit,
    background: @Composable (maxWidth: Dp, maxHeight: Dp) -> Unit = { _, _ -> SoccerField() },
) {
    BoxWithConstraints(modifier = modifier) {
        val centerCircleRadius = maxWidth * 0.15f
        val desiredShirtDiameter = centerCircleRadius * 0.9f
        val originalShirtDiameter = 40.dp
        val scaleFactor = (desiredShirtDiameter / originalShirtDiameter).coerceAtLeast(0.5f)

        background(maxWidth, maxHeight)

        PlayerPlacementLayer(
            players = players,
            scaleFactor = scaleFactor,
            onPlayerClick = onPlayerClick,
            onPlayerDragStart = onPlayerDragStart,
            onPlayerDrag = onPlayerDrag,
            onPlayerDragEnd = onPlayerDragEnd,
            soccerFieldWidth = maxWidth,
            soccerFieldHeight = maxHeight,
        )
    }
}

@ComponentPreview
@Composable
private fun FormationBoard() {
    SquadBuilderTheme {
        FormationBoard()
    }
}

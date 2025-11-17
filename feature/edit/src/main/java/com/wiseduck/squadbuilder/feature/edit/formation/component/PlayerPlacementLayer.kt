package com.wiseduck.squadbuilder.feature.edit.formation.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wiseduck.squadbuilder.core.model.PlacementModel
import com.wiseduck.squadbuilder.core.ui.component.PlayerChip
import com.wiseduck.squadbuilder.feature.edit.formation.data.getPositionForCoordinates
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PlayerPlacementLayer(
    players: List<PlacementModel>,
    scaleFactor: Float,
    onPlayerDragStart: (Int) -> Unit,
    onPlayerDrag: (Int, Float, Float) -> Unit,
    onPlayerDragEnd: (Int, Float, Float) -> Unit,
    onPlayerClick: (Int) -> Unit,
    soccerFieldWidth: Dp,
    soccerFieldHeight: Dp
) {
    val originalChipWidth = 56.dp
    val originalChipHeight = 64.dp

    players.forEach { player ->
        key(player.slotId) {

            val calculatedX = (soccerFieldWidth * player.coordX) - (originalChipWidth / 2)
            val calculatedY = (soccerFieldHeight * player.coordY) - (originalChipHeight / 2)

            val xOffset = calculatedX.coerceIn(0.dp, soccerFieldWidth - originalChipWidth)
            val yOffset = calculatedY.coerceIn(0.dp, soccerFieldHeight - originalChipHeight)

            Column(
                modifier = Modifier
                    .offset(x = xOffset, y = yOffset),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = getPositionForCoordinates(
                        player.coordX,
                        player.coordY
                    ),
                    color = Color.White
                )

                PlayerChip(
                    modifier = Modifier
                        .scale(scaleFactor)
                        .pointerInput(player.slotId) {
                            coroutineScope {
                                launch {
                                    detectDragGestures(
                                        onDragStart = { onPlayerDragStart(player.slotId) },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            val fieldWidthPx = soccerFieldWidth.toPx()
                                            val fieldHeightPx = soccerFieldHeight.toPx()
                                            val deltaX = dragAmount.x / fieldWidthPx
                                            val deltaY = dragAmount.y / fieldHeightPx
                                            onPlayerDrag(player.slotId, deltaX, deltaY)
                                        },
                                        onDragEnd = {
                                            val fieldWidthPx = soccerFieldWidth.toPx()
                                            val fieldHeightPx = soccerFieldHeight.toPx()
                                            val relativeChipWidth = (originalChipWidth.toPx() * scaleFactor) / fieldWidthPx
                                            val relativeChipHeight = (originalChipHeight.toPx() * scaleFactor) / fieldHeightPx
                                            onPlayerDragEnd(player.slotId, relativeChipWidth, relativeChipHeight)
                                        }
                                    )
                                }
                                launch {
                                    detectTapGestures(
                                        onTap = { onPlayerClick(player.slotId) }
                                    )
                                }
                            }
                        },
                    position = player.playerPosition,
                    number = player.playerBackNumber,
                    name = player.playerName
                )
            }
        }
    }
}
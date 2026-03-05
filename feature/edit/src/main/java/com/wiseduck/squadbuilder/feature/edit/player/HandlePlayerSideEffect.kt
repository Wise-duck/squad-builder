package com.wiseduck.squadbuilder.feature.edit.player

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun HandlePlayerSideEffect(
    state: PlayerUiState,
    eventSink: (PlayerUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@LaunchedEffect

        when (effect) {
            is PlayerSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

        eventSink(PlayerUiEvent.InitSideEffect)
    }
}

package com.wiseduck.squadbuilder.feature.edit.player

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun HandlePlayerSideEffect(
    state: PlayerUiState,
    eventSink: (PlayerUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@RememberedEffect

        when (effect) {
            is PlayerSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

        eventSink(PlayerUiEvent.InitSideEffect)
    }
}

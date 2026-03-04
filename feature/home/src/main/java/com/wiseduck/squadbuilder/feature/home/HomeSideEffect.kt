package com.wiseduck.squadbuilder.feature.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeSideEffectsHandler(
    state: HomeUiState,
    eventSink: (HomeUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@LaunchedEffect

        when (effect) {
            is HomeSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

        eventSink(HomeUiEvent.InitSideEffect)
    }
}

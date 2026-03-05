package com.wiseduck.squadbuilder.feature.settings.profile

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun HandleProfileSideEffect(
    state: ProfileUiState,
    eventSink: (ProfileUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@LaunchedEffect

        when (effect) {
            is ProfileSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

        eventSink(ProfileUiEvent.InitSideEffect)
    }
}

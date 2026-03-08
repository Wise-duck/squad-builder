package com.wiseduck.squadbuilder.feature.settings.profile

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun HandleProfileSideEffect(
    state: ProfileUiState,
    eventSink: (ProfileUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@RememberedEffect

        when (effect) {
            is ProfileSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

        eventSink(ProfileUiEvent.InitSideEffect)
    }
}

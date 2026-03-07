package com.wiseduck.squadbuilder.feature.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun HandleHomeSideEffect(
    sideEffect: HomeSideEffect?,
    eventSink: (HomeUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(sideEffect) {
        val effect = sideEffect ?: return@RememberedEffect

        when (effect) {
            is HomeSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

        eventSink(HomeUiEvent.InitSideEffect)
    }
}

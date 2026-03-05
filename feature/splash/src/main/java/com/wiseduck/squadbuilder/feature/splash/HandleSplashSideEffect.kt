package com.wiseduck.squadbuilder.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.wiseduck.squadbuilder.core.common.extensions.goToPlayStore

@Composable
fun HandleSplashSideEffect(
    state: SplashUiState,
    eventSink: (SplashUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@LaunchedEffect

        when (effect) {
            SplashSideEffect.OnUpdateClick -> {
                context.goToPlayStore()
            }
        }

        eventSink(SplashUiEvent.InitSideEffect)
    }
}

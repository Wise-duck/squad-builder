package com.wiseduck.squadbuilder.feature.splash

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.feature.screens.SplashScreen
import dagger.assisted.AssistedFactory
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Inject

class SplashPresenter @Inject constructor(

): Presenter<SplashUiState> {

    @Composable
    override fun present(): SplashUiState {

        return SplashUiState(

        )
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            navigator: Navigator
        ): SplashPresenter
    }
}
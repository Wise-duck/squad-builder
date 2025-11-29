package com.wiseduck.squadbuilder.feature.webview

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.feature.screens.WebViewScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class WebViewPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: WebViewScreen,
) : Presenter<WebViewUiState> {
    @Composable
    override fun present(): WebViewUiState {
        fun handleEvent(event: WebViewUiEvent) {
            when (event) {
                is WebViewUiEvent.OnBackButtonClick -> {
                    navigator.pop()
                }
            }
        }

        return WebViewUiState(
            url = screen.url,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(WebViewScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: WebViewScreen,
            navigator: Navigator,
        ): WebViewPresenter
    }
}

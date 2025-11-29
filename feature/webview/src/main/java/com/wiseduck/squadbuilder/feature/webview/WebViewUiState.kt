package com.wiseduck.squadbuilder.feature.webview

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class WebViewUiState(
    val url: String,
    val eventSink: (WebViewUiEvent) -> Unit,
) : CircuitUiState

sealed interface WebViewUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : WebViewUiEvent
}

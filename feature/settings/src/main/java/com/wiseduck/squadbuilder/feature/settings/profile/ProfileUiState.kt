package com.wiseduck.squadbuilder.feature.settings.profile

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class ProfileUiState(
    val isLoading: Boolean,
    val errorMessage: String? = null,
    val userName: String,
    val eventSink: (ProfileUiEvent) -> Unit
) : CircuitUiState

sealed interface ProfileUiEvent : CircuitUiEvent {
    data object OnLogoutButtonClick : ProfileUiEvent
    data object OnWithDrawButtonClick : ProfileUiEvent
    data object OnDialogCloseButtonClick : ProfileUiEvent
    data class OnTabSelect(
        val screen: Screen
    ) : ProfileUiEvent
    data object OnPrivacyPolicyButtonClick : ProfileUiEvent  // 추후 다른 화면으로 이동
}
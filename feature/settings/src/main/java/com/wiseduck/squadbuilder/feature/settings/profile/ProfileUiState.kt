package com.wiseduck.squadbuilder.feature.settings.profile

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.core.common.utils.UiText

data class ProfileUiState(
    val isLoading: Boolean,
    val isLoggedIn: Boolean = false,
    val userName: String,
    val sideEffect: ProfileSideEffect? = null,
    val eventSink: (ProfileUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface ProfileSideEffect {
    data class ShowToast(
        val message: UiText,
    ) : ProfileSideEffect
}

sealed interface ProfileUiEvent : CircuitUiEvent {
    data object InitSideEffect : ProfileUiEvent

    data object OnLogoutButtonClick : ProfileUiEvent

    data object OnWithDrawButtonClick : ProfileUiEvent

    data class OnTabSelect(
        val screen: Screen,
    ) : ProfileUiEvent

    data object OnPrivacyPolicyButtonClick : ProfileUiEvent
}

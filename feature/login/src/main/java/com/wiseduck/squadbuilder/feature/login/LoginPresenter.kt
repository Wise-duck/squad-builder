package com.wiseduck.squadbuilder.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.common.utils.handleException
import com.wiseduck.squadbuilder.core.data.api.repository.AuthRepository
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import com.wiseduck.squadbuilder.feature.screens.LoginScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val authRepository: AuthRepository,
) : Presenter<LoginUiState> {

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LoginPresenter
    }

    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        var sideEffect by remember { mutableStateOf<LoginSideEffect?>(null) }

        fun handleEvent(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is LoginUiEvent.OnKakaoLoginButtonClick -> {
                    sideEffect = LoginSideEffect.LaunchKakaoLogin
                }

                is LoginUiEvent.OnLoginSuccess -> {
                    scope.launch {
                        authRepository.login(event.accessToken)
                            .onSuccess {
                                navigator.resetRoot(HomeScreen)
                            }
                            .onFailure { exception ->
                                handleException(
                                    exception = exception,
                                    onError = { uiText ->
                                        sideEffect = LoginSideEffect.ShowToast(uiText)
                                    },
                                )
                            }
                    }
                }

                is LoginUiEvent.OnLoginFailure -> {
                    sideEffect = LoginSideEffect.ShowToast(event.message)
                }

                LoginUiEvent.OnLoginRequired -> {
                    navigator.resetRoot(LoginScreen)
                }
            }
        }

        return LoginUiState(
            eventSink = ::handleEvent,
            sideEffect = sideEffect,
        )
    }
}

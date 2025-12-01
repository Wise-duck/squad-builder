package com.wiseduck.squadbuilder.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
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
    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()

        val loginErrorServerConnection = stringResource(R.string.login_error_server_connection)

        var errorMessage by remember { mutableStateOf<String?>(null) }
        var sideEffect by remember { mutableStateOf<LoginSideEffects?>(null) }

        fun handleEvent(event: LoginUiEvent) {
            when (event) {
                is LoginUiEvent.OnKakaoLoginButtonClick -> {
                    errorMessage = null
                    sideEffect = LoginSideEffects.LaunchKakaoLogin
                }

                is LoginUiEvent.OnCloseDialogButtonClick -> {
                    errorMessage = null
                }

                is LoginUiEvent.OnLoginFailure -> {
                    errorMessage = event.errorMessage
                }

                is LoginUiEvent.OnLoginSuccess -> {
                    scope.launch {
                        authRepository.login(event.accessToken)
                            .onSuccess {
                                navigator.resetRoot(HomeScreen)
                            }
                            .onFailure {
                                errorMessage = loginErrorServerConnection
                            }
                    }
                }

                LoginUiEvent.InitSideEffect -> {
                    sideEffect = null
                }
            }
        }

        return LoginUiState(
            errorMessage = errorMessage,
            eventSink = ::handleEvent,
            sideEffect = sideEffect,
        )
    }

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LoginPresenter
    }
}

package com.wiseduck.squadbuilder.feature.settings.profile

import android.util.Log
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
import com.wiseduck.squadbuilder.core.common.constants.WebViewUrls
import com.wiseduck.squadbuilder.core.data.api.repository.AuthRepository
import com.wiseduck.squadbuilder.feature.screens.LoginScreen
import com.wiseduck.squadbuilder.feature.screens.ProfileScreen
import com.wiseduck.squadbuilder.feature.screens.WebViewScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import com.wiseduck.squadbuilder.feature.settings.R

class ProfilePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val authRepository: AuthRepository
) : Presenter<ProfileUiState> {
    @Composable
    override fun present(): ProfileUiState {
        val scope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val logoutErrorServerConnection = stringResource(R.string.logout_error_server_connection)
        val withdrawErrorServerConnection = stringResource(R.string.withdraw_error_server_connection)

        fun handleEvent(event: ProfileUiEvent) {
            when (event) {
                is ProfileUiEvent.OnLogoutButtonClick -> {
                    isLoading = true
                    scope.launch {
                        authRepository.logout()
                            .onSuccess {
                                isLoading = false
                                Log.d("ProfilePresenter", "로그아웃 성공")
                                navigator.resetRoot(LoginScreen)
                            }
                            .onFailure {
                                isLoading = false
                                errorMessage = logoutErrorServerConnection
                                Log.e("ProfilePresenter", "로그아웃 실패: $it")
                            }
                    }
                }

                is ProfileUiEvent.OnWithDrawButtonClick -> {
                    isLoading = true
                    scope.launch {
                        authRepository.withdraw()
                            .onSuccess {
                                isLoading = false
                                Log.d("ProfilePresenter", "계정 탈퇴 성공")
                            }
                            .onFailure {
                                isLoading = false
                                errorMessage = withdrawErrorServerConnection
                                Log.e("ProfilePresenter", "계정 탈퇴 실패: $it")
                            }
                    }
                }

                is ProfileUiEvent.OnDialogCloseButtonClick -> {
                    errorMessage = null
                }

                is ProfileUiEvent.OnTabSelect -> {
                    navigator.resetRoot(event.screen)
                }

                ProfileUiEvent.OnPrivacyPolicyButtonClick -> {
                    val webView = WebViewUrls.PRIVACY_POLICY

                    navigator.goTo(WebViewScreen(
                        url = webView.url
                    ))
                }
            }
        }

        return ProfileUiState(
            isLoading = isLoading,
            errorMessage = errorMessage,
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(ProfileScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator) : ProfilePresenter
    }
}
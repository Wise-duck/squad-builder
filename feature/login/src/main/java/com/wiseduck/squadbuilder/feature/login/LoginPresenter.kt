package com.wiseduck.squadbuilder.feature.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val authRepository: AuthRepository,
) : Presenter<LoginUiState> {
    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val context = LocalContext.current
        val loginErrorKakaoFailed = stringResource(R.string.login_error_kakao_failed)
        val loginErrorServerConnection = stringResource(R.string.login_error_server_connection)

        fun handleLoginResult(
            token: OAuthToken?,
            error: Throwable?,
            scope: CoroutineScope,
        ) {
            if (error != null) {
                Log.e("KAKAO_LOGIN", "로그인 실패", error)
                errorMessage = loginErrorKakaoFailed
            } else if (token != null) {
                scope.launch {
                    authRepository.login(token.accessToken)
                        .onSuccess {
                            Log.i("KAKAO_LOGIN", "로그인 및 토큰 저장 성공")
                            navigator.resetRoot(HomeScreen)
                        }
                        .onFailure { error ->
                            Log.e("KAKAO_LOGIN", "서버 로그인 또는 토큰 저장 실패", error)
                            errorMessage = loginErrorServerConnection
                        }
                }
            }
        }

        fun handleKakaoLogin(
            scope: CoroutineScope,
            context: Context,
        ) {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    handleLoginResult(token, error, scope)
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    handleLoginResult(token, error, scope)
                }
            }
        }

        fun handleEvent(event: LoginUiEvent) {
            when (event) {
                is LoginUiEvent.OnKakaoLoginButtonClick -> {
                    errorMessage = null
                    handleKakaoLogin(scope, context)
                }

                is LoginUiEvent.OnCloseDialogButtonClick -> {
                    errorMessage = null
                }
            }
        }

        return LoginUiState(
            errorMessage = errorMessage,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LoginPresenter
    }
}

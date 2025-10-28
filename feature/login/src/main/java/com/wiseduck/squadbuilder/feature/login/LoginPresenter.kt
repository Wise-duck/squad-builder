package com.wiseduck.squadbuilder.feature.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val authRepository: AuthRepository
) : Presenter<LoginUiState> {

    private fun handleKakaoLogin(scope: CoroutineScope, context: Context) {
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

    private fun handleLoginResult(token: OAuthToken?, error: Throwable?, scope: CoroutineScope) {
        if (error != null) {
             Log.e("KAKAO_LOGIN", "로그인 실패", error)
        } else if (token != null) {
            scope.launch {
                try {
                    authRepository.login(token.accessToken)
                    navigator.resetRoot(HomeScreen)
                    Log.i("KAKAO_LOGIN", "로그인 성공")
                } catch (e: Exception) {
                    Log.e("KAKAO_LOGIN", "로그인 실패: $e")
                }
            }
        }
    }

    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        
        fun handleEvent(event: LoginUiEvent) {
            when (event) {
                is LoginUiEvent.OnKakaoLoginButtonClick -> {
                    handleKakaoLogin(scope, context)
                }
            }
        }

        return LoginUiState(
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator) : LoginPresenter
    }
}
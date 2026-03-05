package com.wiseduck.squadbuilder.feature.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.wiseduck.squadbuilder.core.common.utils.handleException
import com.wiseduck.squadbuilder.feature.login.LoginUiEvent.OnLoginSuccess

@Composable
fun HandleLoginSideEffect(
    state: LoginUiState,
    eventSink: (LoginUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@LaunchedEffect

        when (effect) {
            is LoginSideEffect.LaunchKakaoLogin -> {
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        handleException(
                            exception = error,
                            onError = { uiText ->
                                eventSink(LoginUiEvent.OnLoginFailure(uiText))
                            },
                            onLoginRequired = {
                                eventSink(LoginUiEvent.OnLoginRequired)
                            },
                        )
                    } else if (token != null) {
                        eventSink(OnLoginSuccess(token.accessToken))
                    }
                }

                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                }
            }

            is LoginSideEffect.ShowToast -> {
                Toast.makeText(context, effect.messge.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

        eventSink(LoginUiEvent.InitSideEffect)
    }
}

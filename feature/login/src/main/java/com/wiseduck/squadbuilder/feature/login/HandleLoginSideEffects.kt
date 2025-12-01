package com.wiseduck.squadbuilder.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.wiseduck.squadbuilder.feature.login.LoginUiEvent.OnLoginFailure
import com.wiseduck.squadbuilder.feature.login.LoginUiEvent.OnLoginSuccess

@Composable
fun HandleLoginSideEffects(
    state: LoginUiState,
) {
    val context = LocalContext.current
    val loginErrorKakaoFailed = stringResource(R.string.login_error_kakao_failed)

    LaunchedEffect(state.sideEffect) {
        when (val sideEffect = state.sideEffect) {
            is LoginSideEffects.LaunchKakaoLogin -> {
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        state.eventSink(OnLoginFailure(loginErrorKakaoFailed))
                    } else if (token != null) {
                        state.eventSink(OnLoginSuccess(token.accessToken))
                    }

                    state.eventSink(LoginUiEvent.InitSideEffect)
                }

                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                }
            }

            else -> {}
        }
    }
}

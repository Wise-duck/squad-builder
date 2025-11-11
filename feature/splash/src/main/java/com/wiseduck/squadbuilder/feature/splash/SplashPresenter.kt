package com.wiseduck.squadbuilder.feature.splash

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.common.extensions.goToPlayStore
import com.wiseduck.squadbuilder.core.data.api.repository.RemoteConfigRepository
import com.wiseduck.squadbuilder.feature.screens.LoginScreen
import com.wiseduck.squadbuilder.feature.screens.SplashScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val remoteConfigRepository: RemoteConfigRepository
): Presenter<SplashUiState> {

    @Composable
    override fun present(): SplashUiState {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var isUpdateDialogVisible by remember { mutableStateOf(false) }

        fun handleEvent(event: SplashUiEvent) {
            when (event) {
                SplashUiEvent.OnCloseDialogButtonClick -> {
                    isUpdateDialogVisible = false
                }

                SplashUiEvent.OnUpdateButtonClick -> {
                    scope.launch {
                        context.goToPlayStore()
                    }
                }
            }
        }

        fun checkUpdate() {
            scope.launch {
                remoteConfigRepository.shouldUpdate()
                    .onSuccess {
                        if (it) {
                            isUpdateDialogVisible = true
                        } else {
                            navigator.resetRoot(LoginScreen)
                        }
                    }
                    .onFailure {
                        Log.e("REMOTE_CONFIG", "업데이트 여부 확인 실패: $it")
                    }
            }
        }

        LaunchedEffect(Unit) {
            delay(1000L)
            checkUpdate()
        }

        return SplashUiState(
            isUpdateDialogVisible = isUpdateDialogVisible,
            eventSink = ::handleEvent
        )
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            navigator: Navigator
        ): SplashPresenter
    }
}
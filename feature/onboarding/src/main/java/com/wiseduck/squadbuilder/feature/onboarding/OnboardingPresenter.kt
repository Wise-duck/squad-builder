package com.wiseduck.squadbuilder.feature.onboarding

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.wiseduck.squadbuilder.core.data.api.repository.UserRepository
import com.wiseduck.squadbuilder.feature.screens.LoginScreen
import com.wiseduck.squadbuilder.feature.screens.OnboardingScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

const val ONBOARDING_STEPS = 3

class OnboardingPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val userRepository: UserRepository,
) : Presenter<OnboardingUiState> {
    @Composable
    override fun present(): OnboardingUiState {
        val scope = rememberCoroutineScope()
        val pagerState =
            rememberPagerState(
                pageCount = { ONBOARDING_STEPS },
            )

        fun handleEvent(event: OnboardingUiEvent) {
            when (event) {
                is OnboardingUiEvent.OnNextButtonClick -> {
                    if (event.currentPage == ONBOARDING_STEPS - 1) {
                        scope.launch {
                            userRepository.setOnboardingCompleted(true)
                            navigator.goTo(LoginScreen)
                        }
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(event.currentPage + 1)
                        }
                    }
                }

                OnboardingUiEvent.OnSkipButtonClick -> {
                    navigator.goTo(LoginScreen)
                }
            }
        }

        return OnboardingUiState(
            pagerState = pagerState,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OnboardingPresenter
    }
}

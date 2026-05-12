package com.wiseduck.squadbuilder.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import com.wiseduck.squadbuilder.core.common.events.DialogSpec
import com.wiseduck.squadbuilder.core.common.events.EventHandler
import com.wiseduck.squadbuilder.core.common.events.SquadBuilderEvent
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme
import com.wiseduck.squadbuilder.core.ui.component.SquadBuilderDialog
import com.wiseduck.squadbuilder.feature.screens.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme = isSystemInDarkTheme()

            SquadBuilderTheme(isDarkMode = isDarkTheme) {
                val backStack = rememberSaveableBackStack(SplashScreen)
                val navigator = rememberCircuitNavigator(backStack)

                val dialogSpec = remember { mutableStateOf<DialogSpec?>(null) }

                LaunchedEffect(Unit) {
                    EventHandler.eventFlow.collect { event ->
                        when (event) {
                            is SquadBuilderEvent.ShowDialog -> dialogSpec.value = event.dialogSpec
                        }
                    }
                }

                dialogSpec.value?.let { spec ->
                    SquadBuilderDialog(
                        onDismissRequest = { dialogSpec.value = null },
                        onConfirmRequest = {
                            spec.onConfirm()
                            dialogSpec.value = null
                        },
                        dismissButtonText = spec.dismissButton,
                        confirmButtonText = spec.confirmButton.asString(),
                        title = spec.title?.asString(),
                        description = spec.message.asString(),
                    )
                }

                CircuitCompositionLocals(circuit) {
                    NavigableCircuitContent(
                        modifier = Modifier.fillMaxSize(),
                        backStack = backStack,
                        navigator = navigator,
                        decoratorFactory = remember(navigator) {
                            GestureNavigationDecorationFactory(
                                onBackInvoked = navigator::pop,
                            )
                        },
                    )
                }
            }
        }
    }
}
